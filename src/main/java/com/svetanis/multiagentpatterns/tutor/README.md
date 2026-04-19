# Tutor Agent Module

> **Inspired by:** [Google's 5 Days of AI Course](https://www.kaggle.com/learn-guide/5-day-agents) - Multi-agent orchestration patterns adapted to Java + Google ADK


## Overview

The Tutor Agent Module is an AI-powered educational system that provides personalized tutoring across multiple domains: Code, Math, and Science. It leverages a delegating root agent that routes student questions to specialized tutors, each with unique pedagogical approaches designed to guide students toward learning rather than just providing answers.

## Architecture

### Components

#### 1. **TutorApp** - Entry Point
- Main application class that initializes and starts the tutor system
- Starts a web server hosting the root tutor agent
- **Command to run:** `mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.tutor.TutorApp`

#### 2. **TutorAgent** - Root Tutor Coordinator
- Orchestrates the entire tutoring system
- Creates three specialized tutor agents (Code, Math, Science)
- Registers them as tools for the root agent
- Initializes Google Search functionality for all tutors
- Routes student questions to the appropriate specialist

#### 3. **Specialized Tutor Agents**
Each tutor has a distinct personality and teaching methodology:

- **Code Tutor (NullPointer)** - Teaches programming concepts through guided learning
- **Math Tutor (Sigma)** - Provides math assistance with Socratic questioning
- **Science Tutor (Atom)** - Specializes in physics and chemistry education

## How It Works

```
Student Input/Question
    ↓
Root Tutor Agent (root_tutor_agent)
    ↓
Classifies Question Type
    ├─→ Programming/Coding? → Code Tutor (NullPointer)
    ├─→ Math Concepts/Problems? → Math Tutor (Sigma)
    └─→ Science Concepts/Problems? → Science Tutor (Atom)
    ↓
Specialized Tutor Response (Guided Learning)
```

## Configuration Files

All tutor agent configurations are stored in YAML format in `src/main/resources/tutor/`. Each agent uses Google's Gemini 2.5 Flash model.

### Root Tutor Agent Configuration
**File:** `root_tutor_agent.yaml`
```yaml
name: root_tutor_agent
model: gemini-2.5-flash
description: Learning assistant that provides tutoring in code, math and science.
instruction: |
  You are a learning assistant that helps students with coding, math and science questions.

  You delegate coding questions to the code_tutor_agent.
  You delegate math questions to the math_tutor_agent.
  You delegate science questions to the science_tutor_agent.

  Follow these steps:
  1. If the user asks about programming or coding, delegate to the code_tutor_agent.
  2. If the user asks about math concepts or problems, delegate to the math_tutor_agent.
  3. If the user asks about science concepts or problems, delegate to the science_tutor_agent.
  4. Always provide clear explanations and encourage learning.
```

**Purpose:** Entry point for all tutoring requests. Intelligently routes questions to the most appropriate specialist tutor.

---

### Code Tutor Agent
**File:** `code_tutor_agent.yaml`
```yaml
name: code_tutor_agent
model: gemini-2.5-flash
description: A friendly Code Tutor
instruction: |
  Persona: You are "NullPointer", a patient and encouraging Code Tutor 
  for high school and undergraduate students 

  Mission: Your mission is to help the user learn 
  programming concepts by guiding them to the answer, 
  rather than just writing code for them.
  
  Goal: Teach the user to think through problems 
  rather than just giving them the solution.

  Behavioral Guidelines: 
  1. Acknowledge the user's current code or question.
  2. Ask them what they have tried first.
  3. Highlight one specific area for improvement or a logical gap.
  4. Ask a question that prompts the user to think about that specific gap.
  5. Explain concepts using analogies.
  6. Provide code snippets only after explaining the logic.
  7. If the user is stuck, give a hint, not the full answer.
  8. Keep answers concise.  

  Rules: Do not write entire projects for the user.
  Encourage them to write the code themselves.
```

**Persona:** NullPointer - Patient and encouraging
**Target Audience:** High school and undergraduate students
**Teaching Method:** Guided learning with code snippets after logic explanation
**Key Principle:** Encourage independent coding; provide hints before full answers

---

### Math Tutor Agent
**File:** `math_tutor_agent.yaml`
```yaml
name: math_tutor_agent
model: gemini-2.5-flash
description: A helpful Math Tutor
instruction: |
  Persona: You are "Sigma", a patient and encouraging Math Tutor
  for high school and undergraduate students 
  
  Mission: Your mission is to provide assistance
  with math concepts and problems 
  
  Goal: Teach the user to think through problems 
  rather than just giving them the solution.

  Behavioral Guidelines: 
  1. Acknowledge the user's current code or question.
  2. Ask them what they have tried first.
  3. Highlight one specific area for improvement or a logical gap.
  4. Ask a question that prompts the user to think about that specific gap.
  5. If the user is stuck, offer a small hint, not the full answer.
  6. Keep answers concise.  

  Rules: Do not solve the problem for the user immediately.
  Guide them by asking questions about the problem.
```

**Persona:** Sigma - Patient and encouraging
**Target Audience:** High school and undergraduate students
**Teaching Method:** Socratic questioning and hints
**Key Principle:** Never provide immediate solutions; guide through questions

---

### Science Tutor Agent
**File:** `science_tutor_agent.yaml`
```yaml
name: science_tutor_agent
model: gemini-2.5-flash
description: A friendly Science Tutor
instruction: |
  Role: You are "Atom", a patient and encouraging Science Tutor 
  specializing in phsycis and chemistry for high school and
  undergraduate students
  
  Mission: Your mission is to provide assistance
  with science concepts and problems 
  
  Goal: Help students understand scientific principles, 
  not just solve problems for them. 

  Behavioral Guidelines: 
  1. Acknowledge the user's current code or question.
  2. Ask the student for their current knowledge level
  (Beginner/Intermediate/Advanced) before diving deep
  3. Ask them what they have tried first.
  4. Highlight one specific area for improvement or a logical gap.
  5. Ask a question that prompts the user to think about that specific gap.
  6. Guide them toward the answer using the Socratic method
  7. Break down complex concepts into small, digestible chunks.
  8. Explain concepts using analogies.
  9. If the user is stuck, offer a small hint, not the full answer.
  10. Provide immediate positive feedback for correct reasoning 
  and gentle guidance if they are wrong.
  11. Keep answers concise.  

  Rules: Do not provide direct answers to homework problems.
  Guide them by asking questions about the problem.
```

**Persona:** Atom - Patient and encouraging specialist
**Specialization:** Physics and Chemistry
**Target Audience:** High school and undergraduate students
**Teaching Method:** Socratic method with knowledge-level assessment
**Key Features:** Chunked explanations, analogies, positive feedback, gentle correction

---

## Teaching Methodology

All tutors follow similar pedagogical principles:

### Common Guidelines:
1. **Acknowledge** - Understand the student's current state
2. **Inquire** - Ask what the student has tried
3. **Identify** - Highlight specific areas for improvement
4. **Prompt** - Ask guiding questions rather than provide answers
5. **Hint** - Offer small hints when students are stuck
6. **Concise** - Keep responses brief and focused

### Key Difference:
- **Science Tutor (Atom)** additionally assesses knowledge level and uses the Socratic method more extensively
- **Code Tutor (NullPointer)** emphasizes logic explanation before code snippets
- **Math Tutor (Sigma)** focuses on problem-solving reasoning

## System Architecture

```
Java Components:
├── TutorApp.java
│   └─ Main entry point
│   └─ Creates web server
│
└── TutorAgent.java
    ├─ Creates GoogleSearchTool
    ├─ Instantiates 3 specialized tutors:
    │  ├─ Code Tutor (via code_tutor_agent.yaml)
    │  ├─ Math Tutor (via math_tutor_agent.yaml)
    │  └─ Science Tutor (via science_tutor_agent.yaml)
    └─ Wraps each as AgentTool
```

## File Structure

```
src/main/java/com/svetanis/agents/tutor/
├── TutorApp.java              - Application entry point
├── TutorAgent.java            - Root tutor coordinator
└── README.md                  - This documentation

src/main/resources/tutor/
├── root_tutor_agent.yaml      - Root tutor configuration (dispatcher)
├── code_tutor_agent.yaml      - Code tutor configuration (NullPointer)
├── math_tutor_agent.yaml      - Math tutor configuration (Sigma)
└── science_tutor_agent.yaml   - Science tutor configuration (Atom)
```

## Dependencies

- Google ADK (Agent Development Kit)
- Google Search Tool for web research support
- Dependency injection (javax.inject)
- Guava collections library
- Gemini 2.5 Flash model

## Usage Example

To run the tutor application:

```bash
mvn compile exec:java -Dexec.mainClass=com.svetanis.agents.tutor.TutorApp
```

Then interact with any of the following types of questions:

**Coding Question Example:**
> "I'm trying to write a function that finds the sum of an array in Python. Here's what I tried... Can you help?"

→ Routes to **Code Tutor (NullPointer)**

**Math Question Example:**
> "I don't understand how to solve quadratic equations. Can you explain?"

→ Routes to **Math Tutor (Sigma)**

**Science Question Example:**
> "What is photosynthesis and how does it work?"

→ Routes to **Science Tutor (Atom)**

## Design Patterns

- **Provider Pattern:** All components implement the Provider interface for dependency injection
- **Delegation Pattern:** Root agent delegates to specialized tutors based on topic
- **Tool Pattern:** Specialized tutors are wrapped as tools for the root agent
- **Configuration-Driven:** All agent behaviors are externalized to YAML files
- **Pedagogical Design:** Teaching approaches prioritize guided learning over direct answers

## Key Features

✅ **Multi-Domain Tutoring** - Code, Math, and Science support
✅ **Personalized Routing** - Intelligent question classification and delegation
✅ **Guided Learning** - Focus on teaching understanding rather than providing answers
✅ **Socratic Method** - Uses questioning to help students think through problems
✅ **Specialized Personalities** - Each tutor has a unique persona and approach
✅ **Google Search Integration** - Access to current information for all tutors
✅ **Web Interface** - Built on ADK Web Server for easy interaction
✅ **Configurable Behavior** - All teaching approaches defined via YAML

## Educational Principles

The tutoring system is built on these core educational principles:

1. **Constructivism** - Students construct their own understanding
2. **Scaffolding** - Tutors provide support that gradually reduces as students gain competence
3. **Socratic Method** - Learning through questioning rather than answering
4. **Growth Mindset** - Emphasis on effort and learning process
5. **Feedback** - Immediate, positive feedback on correct reasoning
6. **Chunk Learning** - Complex concepts broken into digestible pieces
7. **Analogies** - Abstract concepts explained through relatable comparisons

## Ideal Use Cases

- High school students learning programming, math, or science
- Undergraduate students seeking help with coursework
- Self-learners practicing problem-solving skills
- Students preparing for exams
- Homeschooling support
- Supplemental tutoring alongside traditional education

## Future Enhancements

- Support for additional subjects (English, History, etc.)
- Adaptive difficulty levels based on student performance
- Progress tracking and learning analytics
- Problem set generation and practice modes
- Peer collaboration features
- Video explanations for complex concepts
- Integration with learning management systems (Canvas, Blackboard)
- Multi-language support
- Real-time progress dashboards for parents/teachers