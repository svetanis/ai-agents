# Currency Agent

## What the project does

The Currency Agent is a component of the `adk-agents` project designed to perform currency conversions and provide exchange rate information. It acts as an intelligent agent capable of fetching and utilizing real-time or cached currency rates to facilitate financial calculations and conversions.

## Why the project is useful

This project provides a robust and extensible solution for integrating currency conversion capabilities into various applications. Its agent-based architecture allows for:

-   **Automated Currency Conversion:** Easily convert amounts between different currencies.
-   **Configurable Rate Providers:** Integrate with various external services or internal data sources for exchange rates.
-   **Modular Design:** As part of the `adk-agents` framework, it can be seamlessly combined with other agents to build complex intelligent systems.
-   **Financial Applications:** Useful for e-commerce platforms, financial reporting tools, international transaction processing, and more.

## How users can get started

To get started with the Currency Agent, follow these steps:

### Prerequisites

-   Java Development Kit (JDK) 11 or higher
-   Apache Maven

### Installation

1.  **Clone the `adk-agents` repository:**

```bash
git clone https://github.com/svetanis/adk-agents.git
cd adk-agents
```

2.  **Build the project:**
    Navigate to the root of the `adk-agents` project and build it using Maven:

```bash
mvn clean install
```

### Configuration

The Currency Agent's behavior is defined in YAML configuration files located in `src/main/resources/currency/`.

-   `currency-agent.yaml`: Main configuration for the `CurrencyAgent`.
-   `calculator-agent.yaml`: Configuration for the `CalculatorAgent` (a sub-component).

You will likely need to configure parameters such as:

-   **API Keys:** If using an external exchange rate provider, API keys will need to be specified in the `currency-agent.yaml` or provided via environment variables.
-   **Default Currencies:** Set default source and target currencies.
-   **Rate Provider Details:** Configure the specific `RateProvider` implementation to use.

### Usage

The main entry point for the Currency Agent is `com.svetanis.agents.currency.CurrencyApp`. You can run this application after building the project.

To run the application, navigate to the `adk-agents` directory and use Maven:
bash
mvn exec:java -Dexec.mainClass="com.svetanis.agents.currency.CurrencyApp"


The application will initialize the agents based on the provided YAML configurations and start listening for tasks or requests.

## Who maintains and contributes

This project is part of the `adk-agents` initiative by [svetanis](https://github.com/svetanis). 