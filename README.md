# Quarkus - Dapr
[![Build](https://github.com/quarkiverse/quarkus-dapr/workflows/Build/badge.svg?branch=main)](https://github.com/quarkiverse/quarkus-dapr/actions?query=workflow%3ABuild)
[![License](https://img.shields.io/github/license/quarkiverse/quarkus-dapr)](http://www.apache.org/licenses/LICENSE-2.0)
[![Central](https://img.shields.io/maven-central/v/io.quarkiverse.dapr/quarkus-dapr-parent?color=green)](https://search.maven.org/search?q=g:io.quarkiverse.dapr%20AND%20a:quarkus-dapr-parent)
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
<!-- ALL-CONTRIBUTORS-BADGE:END -->

## Introduction

### What is Quarkus?

Traditional Java stacks were engineered for monolithic applications with long startup times and large memory
requirements in a world where the cloud, containers, and Kubernetes did not exist. Java frameworks needed to evolve
to meet the needs of this new world.

Quarkus was created to enable Java developers to create applications for a modern, cloud-native world. Quarkus is
a Kubernetes-native Java framework tailored for GraalVM and HotSpot, crafted from best-of-breed Java libraries and
standards. The goal is to make Java the leading platform in Kubernetes and serverless environments while offering
developers a framework to address a wider range of distributed application architectures.

![](https://quarkus.io/assets/images/quarkus_metrics_graphic_bootmem_wide.png)

For more information about Quarkus, please go https://quarkus.io/.

### What is Dapr?

Dapr is a portable, event-driven runtime that makes it easy for any developer to build resilient, stateless and
stateful applications that run on the cloud and edge and embraces the diversity of languages and developer frameworks.

Leveraging the benefits of a sidecar architecture, Dapr helps you tackle the challenges that come with building
microservices and keeps your code platform agnostic.

![](https://dapr.io/images/building-blocks.png)

For more information about Dapr, please go https://dapr.io/.

### What is Quarkus-Dapr?

Quarkus Dapr is a Quarkus extension to integrate with Dapr.

Quarkus Dapr Extension enables Java developers to create ultra lightweight Java native applications for Function
Computing and FaaS scenes, which is also particularly suitable for running as serverless.

With the help of Dapr, these ultra lightweight Java native applications can easily interact with external application
and resources. Dapr provides many useful building blocks to build modern distributed application: service invocation,
state management, input/output bindings, publish & subscribe, secret management......

Because of the advantages of sidecar model, the native applications can benefit from Dapr's distributed capabilities
while remain lightweight without introducing too many dependencies. This is not only helping to keep the size of java
native applications, but also making the native applications easy to build as native images.

### What is NATS
NATS is a connective technology built for the ever increasingly hyper-connected world. It is a single technology that enables applications to securely communicate across any combination of cloud vendors, on-premise, edge, web and mobile, and devices. NATS consists of a family of open source products that are tightly integrated but can be deployed easily and independently. NATS is being used globally by thousands of companies, spanning use-cases including microservices, edge computing, mobile, IoT and can be used to augment or replace traditional messaging.

The NATS Server acts as a central nervous system for building distributed applications. Client APIs are provided in over 40 languages and frameworks including Go, Java, JavaScript/TypeScript, Python, Ruby, Rust, C#, C, and NGINX. Real time data streaming, highly resilient data storage and flexible data retrieval are supported through [JetStream](https://docs.nats.io/jetstream/?_gl=1*1h8mj82*_ga*MTIwNzEyODU2Mi4xNjc4NzUwODg5*_ga_6242VH03CH*MTY3OTIxMTE3OS45LjEuMTY3OTIxMTIwMS4wLjAuMA..) , the next generation streaming platform built into the NATS server. Check out the full list of NATS clients .

NATS was created by Derek Collison, in response to the market need for a simple, secure, and connective technology. NATS is currently deployed in some of the largest cloud platforms, including: VMware, CloudFoundry, Baidu, Siemens, and GE. NATS is 100% free to use under the Apache-2.0 Open Source License.

NATS is unique in its simplicity and performance, and as a result powers some of the largest production environments. You can learn more about NATS in [extensive documentation](https://docs.nats.io/?_gl=1*oh5sid*_ga*MTIwNzEyODU2Mi4xNjc4NzUwODg5*_ga_6242VH03CH*MTY3OTIxMTE3OS45LjEuMTY3OTIxMTIwMS4wLjAuMA..).
![nats-io](https://nats.io/img/logos/nats-horizontal-color.png)

## Examples

With Quarkus Dapr Extension, it's pretty easy for java developers.

### publish & subscribe

To publish events to your message broker, just inject a dapr client to your bean and call it's publishEvent() method:

```java
    @Inject
    SyncDaprClient dapr;

    dapr.publishEvent("messagebus", "topic1", content.getBytes(StandardCharsets.UTF_8), new HashMap<>());
```

To subscribe events for your message broker, adding some annotations on your method is enough:

```java
@POST
@Path("/topic1")
@Topic(name = "topic1", pubsubName = "jetstream-pubsub")
public String eventOnTopic2(String content) {......}
```

For more details and hands-on experiences, please reference to our [Demo](./demo/README.md).

### Notice
In order to user `quarkus-dapr` extension, especially in kts, it has dependency error. `okhttp3` version is need to up-to-date.
It had [issue](https://github.com/dapr/java-sdk/issues/515) already. But how to solve in gradle kotlin. I have a solution.  
```kotlin
configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "com.squareup.okhttp3" && requested.name == "okhttp" && requested.version == "3.14.9") {
                useVersion("4.10.0")
                because("fixes critical bug in 1.2")
            }

        }
    }
```

however, when we change the `okhttp3` version, there was a `okio` version problem.
```kotlin
modules {
        module("com.squareup.okio:okio") {
            replacedBy("com.squareup.okio:okio-jvm", "change it now")
        }
    }
```

I put my complete dependency here.
```kotlin
dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkiverse.dapr:quarkus-dapr:1.0.4")
    implementation("com.squareup.okio:okio-jvm:3.0.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")
    implementation("io.quarkus:quarkus-arc")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")

    modules {
        module("com.squareup.okio:okio") {
            replacedBy("com.squareup.okio:okio-jvm", "change it now")
        }
    }

    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "com.squareup.okhttp3" && requested.name == "okhttp" && requested.version == "3.14.9") {
                useVersion("4.10.0")
                because("fixes critical bug in 1.2")
            }
        }
    }
}
```