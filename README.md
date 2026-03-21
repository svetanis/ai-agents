# 🤖 AI Agents Framework

A powerful, modular, and extensible framework for building **Autonomous AI Agents** using **Java** and **Google's Agent Development Kit (ADK)**. This repository provides production-ready agent implementations that can be easily extended and deployed.

## 🎯 Overview

This framework enables you to create sophisticated autonomous agents capable of:
- **Planning & reasoning** through complex multi-step tasks
- **Tool integration** with external APIs, web search, and local resources
- **Multi-agent collaboration** with specialized role-based agents
- **Human-in-the-loop workflows** with optional approval checkpoints
- **Self-correction** and error recovery capabilities

## ⚙️ Technology Stack

- **Language:** Java
- **Framework:** Google Agent Development Kit (ADK)
- **LLM:** Gemini 2.5 Flash
- **Configuration:** YAML-based agent definitions
- **Execution:** Sequential and Parallel agent orchestration
- **Tools:** Google Search, Web APIs, Custom extensions

## 📚 Agent Modules

Each agent module is built using **Java + Google ADK** and includes:
- Java implementation with dependency injection
- YAML configuration files for agent behavior
- Web server interface via ADK Web Server
- Specialized tools and integrations

### 🎓 [Tutor Agent](https://github.com/svetanis/ai-agents/tree/master/src/main/java/com/svetanis/agents/adk/tutor)

An intelligent **multi-domain tutoring system** that provides personalized educational guidance.

**Use Cases:**
- High school and undergraduate student support
- Programming, Math, and Science tutoring
- Guided learning and problem-solving assistance
- Self-correcting homework help

**Key Features:**
- **3 Specialized Tutors:**
  - 🔤 **Code Tutor (NullPointer)** - Programming concepts via guided learning
  - 📐 **Math Tutor (Sigma)** - Mathematical problem-solving with Socratic method
  - 🔬 **Science Tutor (Atom)** - Physics & Chemistry with knowledge-level adaptation

- **Pedagogical Approach:**
  - Guided learning over direct answers
  - Socratic questioning for critical thinking
  - Chunked explanations with analogies
  - Progressive hint escalation

**Technology:**
```
Java: TutorAgent.java (orchestrator) + TutorApp.java (entry point)
Config: root_tutor_agent.yaml, code_tutor_agent.yaml, math_tutor_agent.yaml, science_tutor_agent.yaml
LLM: Gemini 2.5 Flash
Tools: Google Search
```

**Quick Start:**
```bash
mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.adk.tutor.TutorApp
# Access at: http://localhost:8080
```

[📖 Full Tutor Agent Documentation](https://github.com/svetanis/ai-agents/tree/master/src/main/java/com/svetanis/agents/adk/tutor)

---

### 📝 [Blogger Agent](https://github.com/svetanis/ai-agents/tree/master/src/main/java/com/svetanis/agents/adk/blogger)

> **Inspired by:** [Google's 5 Days of AI Course](https://www.kaggle.com/code/kaggle5daysofai/day-1b-agent-architectures) - Multi-agent orchestration patterns adapted to Java + Google ADK

A **multi-stage content generation pipeline** that produces polished, researched blog posts.

**Use Cases:**
- Automated blog post generation
- Executive briefings and summaries
- Research aggregation across multiple domains
- Content creation for multiple topics simultaneously
- Demonstration of agentic AI patterns

**Key Features:**
- **4-Stage Pipeline:**
  1. 🔍 **Parallel Research** (Tech, Health, Finance)
  2. 📊 **Aggregation** - Synthesize findings into outline
  3. ✍️ **Writing** - Generate engaging content
  4. ✏️ **Editing** - Polish and refine output

- **Research Capabilities:**
  - Real-time Google Search integration
  - Multi-domain expertise (Tech, Health, Finance)
  - Parallel concurrent research execution
  - Citation and source tracking

- **Output Quality:**
  - ~300 word structured blog posts
  - Common theme identification
  - Professional writing standards
  - Grammatically polished content

**Google's 5 Days of AI Concepts Implemented:**
- ✅ **Multi-agent systems** - Specialized agents working together
- ✅ **Agentic workflows** - Sequential + parallel orchestration
- ✅ **Tool use** - Google Search integration
- ✅ **Prompt engineering** - YAML-based behavioral instructions
- ✅ **LLM reasoning** - Multi-step problem solving

**Technology:**
```
Java: BlogPipeline.java (orchestrator), BloggerRootAgent.java, BloggerApp.java
Config: root-agent.yaml, aggregator-agent.yaml, writer-agent.yaml, editor-agent.yaml
        tech-researcher.yaml, health-researcher.yaml, finance-researcher.yaml
LLM: Gemini 2.5 Flash
Tools: Google Search, Parallel/Sequential Agents
```

**Quick Start:**
```bash
mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.adk.blogger.BloggerApp
# Prompt: "Run the daily executive briefing on Tech, Health, and Finance"
```

[📖 Full Blogger Agent Documentation](https://github.com/svetanis/ai-agents/tree/master/src/main/java/com/svetanis/agents/adk/blogger)

---

## 🏗️ Architecture Overview

### Core Framework Components

The framework provides several foundational components used by all agents:

```
src/main/java/com/svetanis/agents/adk/
├── AgentConfig.java           - Configuration data structure
├── AgentConfigProvider.java    - YAML configuration loader
├── AgentContext.java           - Execution context with tools
├── LlmAgentProvider.java       - LLM agent instantiation
├── blogger/                    - Blogger agent implementation
└── tutor/                      - Tutor agent implementation
```

### Agent Execution Flow

```
┌─────────────────────────────────────────────────────┐
│         User Input / API Request                    │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│  App.java (Entry Point)                             │
│  ├─ Initializes Agent                              │
│  └─ Starts ADK Web Server                          │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│  Agent.java (Root Agent Implementation)             │
│  ├─ Loads configuration from YAML                  │
│  ├─ Initializes tools & sub-agents                 │
│  └─ Executes LLM reasoning                         │
└────────────────────┬────────────────────────────────┘
                     │
        ┌────────────┴────────────┬──────────────┐
        │                         │              │
        ▼                         ▼              ▼
   ┌─────────────┐        ┌──────────────┐  ┌──────────┐
   │ Sub-Agent 1 │        │ Sub-Agent 2  │  │ Tool 1   │
   │ (via YAML)  │        │ (via YAML)   │  │ (Search) │
   └─────────────┘        └──────────────┘  └──────────┘
        │                         │
        └────────────┬────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│  Final Output / Response                            │
└─────────────────────────────────────────────────────┘
```

## 🛠️ Advanced Agent Patterns Supported

### 1. **Multi-Agent Teams**
Assign specialized tasks to a group of agents with different roles:
- **Blogger:** Research team + Aggregator + Writer + Editor
- **Tutor:** Specialized tutors for Code/Math/Science

### 2. **Sequential Orchestration**
Agents execute in order, passing outputs as inputs:
```
Research → Aggregation → Writing → Editing
```

### 3. **Parallel Execution**
Multiple agents work simultaneously for efficiency:
```
Tech Researcher ─┐
Health Researcher├─→ Aggregator
Finance Researcher─┘
```

### 4. **Delegation Pattern**
Root agent routes requests to appropriate specialists:
```
Root Tutor Agent
├─→ Code Tutor (for programming questions)
├─→ Math Tutor (for math questions)
└─→ Science Tutor (for science questions)
```

### 5. **Tool Integration**
Agents extend capabilities through tools:
- Google Search for real-time information
- Custom tools for domain-specific operations
- Sub-agents wrapped as tools for composition

### 6. **Self-Correction**
Agents can reflect on output and retry:
- Editor refines writer output
- Socratic feedback for incorrect student answers
- Hint escalation for stuck learners

### 7. **Human-in-the-Loop**
Optional checkpoints for human approval (extensible pattern)

## 📦 Configuration-Driven Design

All agent behaviors are externalized to **YAML configuration files**:

```yaml
name: example_agent
model: gemini-2.5-flash
description: Agent description
instruction: |
  Detailed behavioral instructions
  - Role and persona
  - Mission and goals
  - Specific guidelines
  - Rules and constraints
outputKey: agent_output_variable
```

**Benefits:**
- ✅ No code changes needed to modify agent behavior
- ✅ Easy version control of agent configurations
- ✅ Deploy multiple agent variants instantly
- ✅ Non-technical stakeholders can modify behavior
- ✅ Rapid experimentation and iteration

## 🔧 Getting Started

### Prerequisites
- Java 11+
- Maven 3.6+
- Google Cloud credentials (for Gemini API)

### Installation

```bash
# Clone the repository
git clone https://github.com/svetanis/ai-agents.git
cd ai-agents

# Build the project
mvn clean install

# Run a specific agent
mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.adk.tutor.TutorApp
# or
mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.adk.blogger.BloggerApp
```

### Environment Setup

Set up your Google Cloud credentials:

```bash
export GOOGLE_APPLICATION_CREDENTIALS=/path/to/your/credentials.json
```

## 🚀 Creating New Agents

To create a new agent using this framework:

1. **Create Java Structure:**
   ```java
   public class MyAgent implements Provider<LlmAgent> {
       @Override
       public LlmAgent get() {
           AgentContext ctx = ctx("my-agent");
           return new LlmAgentProvider(ctx).get();
       }
       
       private AgentContext ctx(String fragment) {
           AgentConfig config = new AgentConfigProvider(fragment).get();
           return AgentContext.builder()
                   .withConfig(config)
                   .withTools(/* your tools */)
                   .build();
       }
   }
   ```

2. **Create YAML Configuration:**
   ```yaml
   name: my_agent
   model: gemini-2.5-flash
   description: Your agent description
   instruction: |
     Your detailed instructions...
   ```

3. **Create Entry Point:**
   ```java
   public class MyApp {
       public static void main(String[] args) {
           LlmAgent agent = new MyAgent().get();
           AdkWebServer.start(agent);
       }
   }
   ```

## 📊 Comparison Matrix

| Feature | Tutor | Blogger |
|---------|-------|---------|
| **Purpose** | Educational guidance | Content generation |
| **Inspiration** | Custom design | Google's 5 Days of AI |
| **Sub-agents** | 3 specialized tutors | 3 researchers + aggregator/writer/editor |
| **Orchestration** | Delegation routing | Sequential pipeline |
| **Parallelization** | Sequential | Parallel research |
| **Tools** | Google Search | Google Search |
| **Output Type** | Guided learning | Polished blog post |
| **Domain Coverage** | Code/Math/Science | Tech/Health/Finance |

## 📈 Framework Capabilities

### Agent Features
- ✅ LLM-powered reasoning with Gemini 2.5 Flash
- ✅ Tool integration and extension
- ✅ Multi-agent composition and orchestration
- ✅ Sequential and parallel execution patterns
- ✅ YAML-based configuration management
- ✅ Web API interface via ADK Web Server
- ✅ Dependency injection via Provider pattern
- ✅ Google Search integration for real-time data

### Development Features
- ✅ Java-based implementation (type-safe)
- ✅ Clean, extensible architecture
- ✅ Modular component design
- ✅ Production-ready code patterns
- ✅ Easy testing and debugging
- ✅ Maven build system
- ✅ Clear separation of concerns

## 🔐 Security Considerations

- API credentials managed via environment variables
- YAML configurations should not contain secrets
- Web server can be containerized and deployed securely
- Google Cloud IAM for credential management
- Input validation for user-provided prompts

## 🧪 Testing & Development

Each agent module can be tested independently:

```bash
# Test Tutor Agent
mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.adk.tutor.TutorApp

# Test Blogger Agent
mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.adk.blogger.BloggerApp
```

## 📝 Project Structure

```
ai-agents/
├── README.md                                    (This file)
├── pom.xml                                     (Maven configuration)
├── src/main/java/com/svetanis/agents/adk/
│   ├── AgentConfig.java                        (Configuration model)
│   ├── AgentConfigProvider.java                (YAML loader)
│   ├── AgentContext.java                       (Execution context)
│   ├── LlmAgentProvider.java                   (Agent factory)
│   ├── blogger/                                (Blogger agent)
│   │   ├── BloggerApp.java
│   │   ├── BloggerRootAgent.java
│   │   ├── BlogPipeline.java
│   │   ├── ResearchTeam.java
│   │   └── README.md
│   └── tutor/                                  (Tutor agent)
│       ├── TutorApp.java
│       ├── TutorAgent.java
│       └── README.md
└── src/main/resources/
    ├── blogger/                                (Blogger configurations)
    │   ├── root-agent.yaml
    │   ├── aggregator-agent.yaml
    │   ├── writer-agent.yaml
    │   ├── editor-agent.yaml
    │   ├── tech-researcher.yaml
    │   ├── health-researcher.yaml
    │   ├── finance-researcher.yaml
    │   └── search-agent.yaml
    └── tutor/                                  (Tutor configurations)
        ├── root_tutor_agent.yaml
        ├── code_tutor_agent.yaml
        ├── math_tutor_agent.yaml
        └── science_tutor_agent.yaml
```

## 🎓 Learning Resources

- **Google ADK Documentation:** [Official Docs](https://ai.google.dev/adk)
- **Gemini API:** [API Reference](https://ai.google.dev/api)
- **Google's 5 Days of AI:** [Course Link](https://ai.google.dev/learn)
- **Agent Patterns:** See individual agent READMEs for detailed documentation

## 🤝 Contributing

To contribute new agents or improvements:

1. Follow the established Java + Google ADK patterns
2. Create YAML configurations for behavior
3. Include comprehensive README documentation
4. Test thoroughly before submitting
5. Add your agent to this main README

## 📄 License

This project is provided as-is for educational and development purposes.

## 🎯 Next Steps

- **Explore Tutor Agent:** [View Implementation](src/main/java/com/svetanis/agents/adk/tutor)
- **Explore Blogger Agent:** [View Implementation](src/main/java/com/svetanis/agents/adk/blogger) (based on Google's 5 Days of AI)
- **Create Your Own:** Use the patterns and structure to build specialized agents
- **Extend & Deploy:** Customize agents for your specific use cases

---

**Built with Java + Google Agent Development Kit (ADK)** 🚀