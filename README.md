### Available Language

- [RU](README_RU.md)

# Multi-Agent Systems on the JADE Platform

## Description

This repository contains four lab assignments developed using the JADE 
platform for creating multi-agent systems. 
Each assignment addresses a specific task in different subject areas.

## Contents

1. **Ping Pong**
2. **Book Trading**
3. **Equipment Order Placement**
4. **Warehouse Management**

## 1. Ping Pong

### Description

In this lab assignment, a simple multi-agent system is implemented where 
two agents exchange messages, simulating a ping-pong game. 
Each agent sends a "Ping" or "Pong" message and waits for a response 
from the other agent.

### Files

- `PingAgent.java`
- `PongAgent.java`
- `MainLauncher.java`

### Run

1. Run `MainLauncher` to create and start the Ping and Pong agents.
2. Observe the message exchange in the console.

## 2. Book Trading

### Description

This lab assignment models the process of book trading between buyers and 
sellers. Buyer agents send requests to purchase books, and seller agents 
respond to these requests with offers. Buyers select the best offers and 
make purchases.

### Files

- `BookBuyerAgent.java`
- `BookSellerAgent.java`
- `MainLauncher.java`

### Run

1. Run `MainLauncher` to create and start the buyer and seller agents.
2. Observe the book trading process in the console.

## 3. Equipment Order Placement

### Description

In this lab assignment, the process of selecting equipment for companies 
based on their requirements is modeled. Company agents send requests to 
manufacturers, receive offers, and select the best offers.

### Files

- `AgentCompany.java`
- `AgentManufacturer.java`
- `MainLauncher.java`

### Run

1. Run `MainLauncher` to create and start the company and manufacturer agents.
2. Observe the equipment selection process in the console.

## 4. Warehouse Management

### Description

This lab assignment models the process of warehouse management. 
Agents receive orders, assemble goods, and manage warehouse inventory. 
The warehouse manager agent receives orders, the assembler agents assemble 
and deliver goods, and the warehouse agent manages the inventory.

### Files

- `AgentWarehouseManager.kt`
- `AgentAssembler.kt`
- `AgentWarehouse.kt`
- `AgentExtensions.kt`
- `Utils.kt`
- `MainLauncher.kt`

### Run

1. Run `MainLauncher` to create and start the warehouse manager, assembler, and warehouse agents.
2. Observe the warehouse management process in the console.

## Requirements

To complete these lab assignments, you need to have the JADE platform and JDK installed.

### Installing JADE

There are two ways to install JADE: from the official website or using a fork.

#### Option 1: Installing from the [official JADE website](http://jade.tilab.com/download/jade/)
#### Option 2: Using a [Fork of JADE from GitLab](https://gitlab.com/jade-project)

## Authors

These lab assignments were developed by [Anton Biluta](https://github.com/antonbiluta)
<br>under the guidance of Associate Professor T.A. Prikhodko, Ph.D.
