# Blogger Agent Module

> **Inspired by:** [Google's 5 Days of AI Course](https://www.kaggle.com/learn-guide/5-day-agents) - Multi-agent orchestration patterns adapted to Java + Google ADK

## Overview

The Blogger Module is an AI-powered agent system that generates daily executive briefings on multiple topics (Tech, Health, and Finance). It leverages a multi-agent architecture with specialized research, aggregation, writing, and editing agents to produce comprehensive blog content.

## Architecture

### Components

#### 1. **BloggerApp** - Entry Point
- Main application class that initializes and starts the blogger system
- Starts a web server hosting the root agent
- **Command to run:** `mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.blogger.BloggerApp`
- **Default prompt:** "Run the daily executive briefing on Tech, Health, and Finance"

#### 2. **BloggerRootAgent** - Root Agent
- Orchestrates the entire blogging pipeline
- Integrates the BlogPipeline as a tool
- Manages agent context and configuration
- Loads configuration from `blogger/root-agent` configuration fragment

#### 3. **BlogPipeline** - Sequential Processing Pipeline
A sequential agent that orchestrates the following stages:
1. **ResearchTeam** (Parallel) - Conducts research on multiple topics
2. **Aggregator Agent** - Synthesizes research findings
3. **Writer Agent** - Generates blog content
4. **Editor Agent** - Refines and polishes the final output

#### 4. **ResearchTeam** - Parallel Research Execution
Runs three researcher agents in parallel, each with Google Search capabilities:
- **Tech Researcher** - Researches technology and innovation topics
- **Health Researcher** - Gathers health and wellness information
- **Finance Researcher** - Collects financial market and economic data

## How It Works

```
User Input
    ↓
BloggerRootAgent (Root Agent)
    ↓
BlogPipeline (Sequential)
    ├─→ ResearchTeam (Parallel)
    │   ├─→ Tech Researcher (with Google Search)
    │   ├─→ Health Researcher (with Google Search)
    │   └─→ Finance Researcher (with Google Search)
    ├─→ Aggregator Agent
    ├─→ Writer Agent
    └─→ Editor Agent
    ↓
Blog Post Output
```

## Configuration Files

All agent configurations are stored in YAML format in `src/main/resources/blogger/`. Each agent uses Google's Gemini 2.5 Flash model.

### Root Agent Configuration
**File:** `root-agent.yaml`
```yaml
name: BloggerRootAgent
model: gemini-2.5-flash
description: A Blogger Root Agent that synthesizes the research results and creates blog posts
instruction: |
  Your role is to help bloggers and influencers
  come up with interesting topic ideas,
  to search information about the topic,
  and craft a compelling social media post.
```

**Purpose:** Entry point for the entire blog generation system. Coordinates all downstream agents through the BlogPipeline tool.

---

### Research Agents

#### Tech Researcher
**File:** `tech-researcher.yaml`
```yaml
name: TechResearcher
model: gemini-2.5-flash
description: An agent that searches and dives in AI and ML topics
instruction: |
  Research the latest AI/ML trends.
  Include 3 key developments, the main
  companies involved, and the potential impact.
  Use the `google-search-agent` tool to
  search up-to-date and relevant information 
  on the given topic and present the findings with citations.
  Keep the report concise (100 words).
outputKey: tech_research
```

**Purpose:** Researches latest AI/ML trends using Google Search. Outputs to `tech_research` key.

#### Health Researcher
**File:** `health-researcher.yaml`
```yaml
name: HealthResearcher
model: gemini-2.5-flash
description: An agent that searches and dives into medical topics
instruction: |
  Research recent medical breakthroughs.
  Include 3 significant advances, their 
  practical applications, and estimated timelines.
  Use the `google-search-agent` tool to
  search up-to-date and relevant information 
  on the given topic and present the findings with citations.
  Keep the report concise (100 words).
outputKey: health_research
```

**Purpose:** Researches recent medical breakthroughs. Outputs to `health_research` key.

#### Finance Researcher
**File:** `finance-researcher.yaml`
```yaml
name: FinanceResearcher
model: gemini-2.5-flash
description: An agent that searches and dives into financial topics
instruction: |
  Research the current fintech trends.
  Include 3 key trends, their market
  implications, and the future outlook.
  Use the `google-search-agent` tool to
  search up-to-date and relevant information 
  on the given topic and present the findings with citations.
  Keep the report concise (100 words).
outputKey: finance_research
```

**Purpose:** Researches current fintech trends. Outputs to `finance_research` key.

---

### Processing Agents

#### Aggregator Agent
**File:** `aggregator-agent.yaml`
```yaml
name: AggregatorAgent
model: gemini-2.5-flash
description: An agent that synthesize the research results
instruction: |
  Combine these three research findings
  into a single blog outline:
  
  **Technology Trends:**
  {tech_research}
  
  **Health Breakthroughts:**
  {health_research}
  
  **Finance Innovations:**
  {finance_research}
  
  Your summary should highlight common themes,
  surprising connections, and the most important
  key takeaways from all three reports. The
  final summary should be around 300 words.
outputKey: summary_outline
```

**Purpose:** Synthesizes research from all three researchers into a coherent outline. Takes research outputs as inputs and produces `summary_outline`.

#### Writer Agent
**File:** `writer-agent.yaml`
```yaml
name: WriterAgent
model: gemini-2.5-flash
description: An agent that writes the full blog post based on the outline from the previous agent
instruction: |
  Following this outline strictly: {summary_outline}
  write a brief, 300-word blog post with
  an engaging and informative tone.
outputKey: blog_draft
```

**Purpose:** Writes the full blog post based on the aggregator's outline. Produces `blog_draft`.

#### Editor Agent
**File:** `editor-agent.yaml`
```yaml
name: EditorAgent
model: gemini-2.5-flash
description: An agent that edits and polishes the draft from the writer agent
instruction: |
  Edit this draft: {blog_draft}
  Your task is to polish the text by fixing
  any grammatical errors, improving the flow
  and sentence structure, and enhancing
  overall clarity.
outputKey: final_blog
```

**Purpose:** Polish and refine the blog draft. Produces `final_blog` - the final output.

---

### Supporting Agent

#### Google Search Agent
**File:** `search-agent.yaml`
```yaml
name: google_search_agent
model: gemini-2.5-flash
description: An agent that searches on Google Search
instruction: |
  Your role is to search on Google Search.
  Use the Google Search Tool to search up-to-date
  and relevant information about the topic.
  Focus: Maintain strict relevance to the requested topic. 
  No Hallucinations: If information is not available, 
  state that you do not know rather than creating a fake answer.
```

**Purpose:** Used by all three researcher agents to perform Google searches. Ensures accurate, up-to-date information with proper citations.

---

## Data Flow

```
Research Phase (Parallel Execution):
  tech-researcher.yaml   ──┐
  health-researcher.yaml ──┼──> Outputs: tech_research, health_research, finance_research
  finance-researcher.yaml ─┘

Aggregation Phase:
  aggregator-agent.yaml (uses above outputs) ──> Output: summary_outline

Writing Phase:
  writer-agent.yaml (uses summary_outline) ──> Output: blog_draft

Editing Phase:
  editor-agent.yaml (uses blog_draft) ──> Output: final_blog (FINAL RESULT)
```

## Key Features

- **Multi-Topic Coverage:** Simultaneously researches and covers Tech, Health, and Finance topics
- **Parallel Research:** Uses parallel agents for efficient concurrent research
- **Sequential Pipeline:** Ensures content flows through research → aggregation → writing → editing
- **Search Integration:** Incorporates Google Search for real-time information gathering
- **Web Interface:** Built on ADK Web Server for easy interaction
- **Configurable Agents:** All agent behaviors defined via YAML for easy customization

## File Structure

```
src/main/java/com/svetanis/agents/adk/blogger/
├── BloggerApp.java              - Application entry point
├── BloggerRootAgent.java         - Root agent implementation
├── BlogPipeline.java             - Sequential pipeline implementation
├── ResearchTeam.java             - Parallel research team implementation
└── README.md                     - This documentation

src/main/resources/blogger/
├── root-agent.yaml               - Root agent configuration
├── aggregator-agent.yaml         - Aggregator agent configuration
├── writer-agent.yaml             - Writer agent configuration
├── editor-agent.yaml             - Editor agent configuration
├── tech-researcher.yaml          - Tech researcher configuration
├── health-researcher.yaml        - Health researcher configuration
├── finance-researcher.yaml       - Finance researcher configuration
├── search-agent.yaml             - Google Search agent configuration
└── sample-output.md              - Example of generated blog output
```

## Dependencies

- Google ADK (Agent Development Kit)
- Google Search Tool for web research
- Dependency injection (javax.inject)
- Guava collections library
- Gemini 2.5 Flash model

## Usage Example

To run the blogger application and request an executive briefing:

```bash
mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.blogger.BloggerApp
```

Then prompt the web server with:
> "Run the daily executive briefing on Tech, Health, and Finance"

The system will:
1. Research current AI/ML, medical, and fintech trends in parallel
2. Aggregate the findings into a structured outline
3. Write a comprehensive 300-word blog post
4. Edit and polish the final output

## Design Patterns

- **Provider Pattern:** All components implement the Provider interface for dependency injection
- **Builder Pattern:** Agents are constructed using builder APIs
- **Sequential/Parallel Agents:** Combines sequential and parallel execution strategies
- **Tool Integration:** Wraps the blog pipeline as an agent tool
- **Configuration-Driven:** All agent behaviors are externalized to YAML files

## Output

The final output includes:
- A professionally written 300-word blog post
- Content covering three major domains (Tech, Health, Finance)
- Properly cited information from web searches
- Grammatically correct and well-structured prose
- Engaging and informative tone suitable for social media

## Future Enhancements

- Support for additional topic domains beyond Tech/Health/Finance
- Customizable output formats (HTML, PDF, JSON, social media templates)
- Scheduling for automated daily/weekly briefings
- Content caching and versioning
- Multi-language support
- Customizable report lengths and complexity levels
- Integration with publishing platforms (Medium, LinkedIn, etc.)
