# ADK Agents

## What the project does

This repository, `adk-agents`, showcases a collection of diverse Artificial Intelligence (AI) agents built using the Agent Development Kit (ADK). The project provides practical examples of how to design, configure, and run intelligent agents for various tasks, demonstrating the power and flexibility of the ADK framework. This project specifically leverages **Google's Gemini models** through the ADK and integrates with **GitHub Copilot** for development assistance.

Each agent in this repository is designed to address a specific problem domain, offering insights into multi-agent systems, tool integration, and prompt engineering within the ADK ecosystem, with a focus on the Google AI platform.

## Why the project is useful

`adk-agents` serves as an invaluable resource for developers looking to:

*   **Learn ADK**: Understand the core concepts and implementation patterns of the Agent Development Kit through concrete, runnable examples, particularly with Google's AI models.
*   **Build Custom Agents**: Get a head start on developing their own AI agents by adapting existing examples, specifically tailored for Gemini.
*   **Explore Agent Capabilities**: See how different agents can collaborate or function independently to achieve complex goals using the ADK and Google Gemini.
*   **Rapid Prototyping**: Quickly set up and experiment with various agent configurations using declarative YAML definitions.
*   **Leverage Google AI**: Understand how to integrate and utilize Google Gemini models effectively within agent-based applications.

## ⚙️ Technology Stack

- **Language:** Java
- **Framework:** Google Agent Development Kit (ADK)
- **LLM:** Gemini 2.5 Flash
- **Configuration:** YAML-based agent definitions
- **Execution:** Sequential and Parallel agent orchestration
- **Tools:** Google Search, Web APIs, Custom extensions, MCP Toolset

## Key features include:

*   **Modular Agent Design**: Each agent is independently structured, making it easy to understand, modify, and extend.
*   **Declarative Configuration**: Agents are configured using YAML files, defining their roles, goals, and the tools they can utilize.
*   **Diverse Agent Examples**:
    *   **Blogger Agent**: A sophisticated multi-agent system capable of researching topics, generating blog post drafts, refining content, and publishing.
    *   **Currency Agent**: An agent designed for currency conversion and calculations, demonstrating tool use for external data fetching.
    *   **GitHub Readme Agent**: An agent that can analyze a GitHub repository and generate a comprehensive `README.md` file (meta-!).
    *   **Tutor Agent**: An interactive educational agent capable of providing tutoring in various subjects.
*   **Java-based Implementation**: Leverages the robust Java ecosystem for building scalable and performant agent applications.
*   **Google Gemini Integration**: All agents are configured to work seamlessly with Google Gemini as the underlying Large Language Model.

## How users can get started

To get started with the `adk-agents` project, follow these steps:

### Prerequisites

*   **Java Development Kit (JDK) 17 or higher**: Ensure you have a compatible JDK installed.
*   **Apache Maven 3.6.3 or higher**: Maven is used for building and managing the project.
*   **Google AI Studio / Gemini API Key**: All agents require access to Google Gemini. You will need to set up an environment variable for your Google API key: `GOOGLE_API_KEY`. Refer to the specific agent's `README.md` for exact environment variable names if different.

### Installation

1.  **Clone the repository**:
    ```
bash
git clone https://github.com/svetanis/adk-agents.git
cd adk-agents


2.  **Build the project**:
    Navigate to the project root directory and build the project using Maven:
    ```
bash
mvn clean install

    This command compiles the Java code, runs tests, and packages the agents.

### Usage Examples

Each agent can be run independently. Below are examples for running each agent. Before running, ensure your `GOOGLE_API_KEY` environment variable is set correctly.

#### Blogger Agent

The Blogger Agent is a multi-agent system that helps in generating blog posts.

To run the Blogger Agent:
bash

Example: Set your Google API key (replace with your actual key)
export GOOGLE_API_KEY="YOUR_GOOGLE_API_KEY"

Run the Blogger Application
mvn exec:java -Dexec.mainClass="com.svetanis.agents.blogger.BloggerApp"


For more detailed instructions and agent configuration, refer to the [Blogger Agent's README](src/main/java/com/svetanis/agents/blogger/README.md).

#### Currency Agent

The Currency Agent provides currency conversion functionalities.

To run the Currency Agent:
bash

Example: Set your Google API key (replace with your actual key)
export GOOGLE_API_KEY="YOUR_GOOGLE_API_KEY"

Run the Currency Application
mvn exec:java -Dexec.mainClass="com.svetanis.agents.currency.CurrencyApp"


#### GitHub Readme Agent

This agent can generate a `README.md` file for a given GitHub repository.

To run the GitHub Readme Agent:
bash

Example: Set your Google API key (replace with your actual key)
export GOOGLE_API_KEY="YOUR_GOOGLE_API_KEY"

Run the GitHub Application
mvn exec:java -Dexec.mainClass="com.svetanis.agents.github.GithubApp"


#### Tutor Agent

The Tutor Agent helps users learn various subjects.

To run the Tutor Agent:
bash

Example: Set your Google API key (replace with your actual key)
export GOOGLE_API_KEY="YOUR_GOOGLE_API_KEY"

Run the Tutor Application
mvn exec:java -Dexec.mainClass="com.svetanis.agents.tutor.TutorApp"


For more detailed instructions and agent configuration, refer to the [Tutor Agent's README](src/main/java/com/svetanis/agents/tutor/README.md).

## Where users can get help

For assistance with `adk-agents`, consider the following resources:

*   **ADK Documentation**: For general information and guides on the Agent Development Kit, refer to the [official ADK documentation](https://github.com/Azure/adk).
*   **Google AI Studio Documentation**: For specific details on using Google Gemini models, refer to the [Google AI Studio documentation](https://ai.google.dev/).
*   **GitHub Issues**: If you encounter any issues, have questions, or want to suggest new features, please open an issue on the [GitHub Issues page](https://github.com/svetanis/adk-agents/issues).

## Who maintains and contributes

### Maintainer

*   **svetanis** ([@svetanis](https://github.com/svetanis))

### Contributing

We welcome contributions to the `adk-agents` project! If you're interested in contributing, please consider:

*   Forking the repository.
*   Creating a new branch for your features or bug fixes.
*   Submitting a pull request with a clear description of your changes.

For more detailed contribution guidelines, please refer to a future `CONTRIBUTING.md` file.
