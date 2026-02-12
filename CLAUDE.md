# CLAUDE.md

## Project Overview

**aixm** is an AIXM 5.1.1 XML parser module that uses JAXB (Jakarta XML Binding) to generate typed Java classes from the official AIXM XSD schemas at build time. It is consumed by [air-app](https://github.com/algonents/air) as a Gradle composite build dependency.

## Build

```bash
./gradlew build          # Generate JAXB classes, compile, and run tests
./gradlew generateJaxb   # Only run XSD → Java code generation
```

## Tech Stack

- **Language:** Kotlin 2.2.20 (JVM)
- **JAXB:** Jakarta XML Binding API 4.0.2 + GlassFish runtime 4.0.5
- **Code Generation:** XJC (JAXB compiler) 4.0.5, invoked via Gradle `JavaExec` task
- **Testing:** Kotlin Test JUnit

## Project Structure

```
src/main/resources/
├── aixm-5.1.1/                    # AIXM 5.1.1 XSD schemas (from aixm.aero)
│   ├── AIXM_Features.xsd         # Core AIXM feature types
│   ├── AIXM_DataTypes.xsd        # AIXM data type definitions
│   ├── AIXM_AbstractGML_ObjectTypes.xsd
│   ├── binding.xml                # JAXB binding customizations
│   ├── gml/3.2.1/                # OGC GML 3.2.1 schemas
│   ├── message/                  # AIXM BasicMessage & ADR Message schemas
│   ├── iso/19139/                # ISO 19139 metadata schemas
│   └── extensions/ADR-23.5.0/    # ADR extension schemas
└── xlink/                         # W3C XLink & XML schemas

src/test/
├── kotlin/com/algonents/aixm/
│   └── AixmParserTest.kt         # Unmarshal test with sample Airport XML
└── resources/samples/
    └── Airport.xml                # Sample AIXM BasicMessage (EADD airport + runway)

build/generated/sources/jaxb/     # Generated at build time (not committed)
├── aero/aixm/_5_1_1/             # AIXM feature & type classes
├── com/algonents/aixm/
│   ├── datatypes/                # Package for AIXM_DataTypes.xsd
│   └── message/                  # Package for AIXM_BasicMessage.xsd
├── com/algonents/gml/            # Package for GML 3.2.1
├── org/isotc211/_2005/           # ISO 19139 generated classes
└── org/w3/_1999/xlink/           # XLink generated classes
```

## How It Works

1. The `generateJaxb` Gradle task runs XJC against `AIXM_Features.xsd` and `AIXM_BasicMessage.xsd`
2. `binding.xml` resolves JAXB naming conflicts (GML element/type collisions, XLink title duplicates) and assigns custom package names
3. Generated Java classes are placed in `build/generated/sources/jaxb/` and added to the main source set
4. Both `compileJava` and `compileKotlin` depend on `generateJaxb`

## JAXB Binding Customizations

The `binding.xml` handles three categories of conflicts:

- **XLink:** Renames duplicate `xlink:title` properties (`locatorTitle`, `arcTitle`)
- **GML element/type collisions:** Renames elements that clash with same-named types (e.g., `Ellipsoid` → `ellpsoidElement`, `CartesianCS` → `CartesianCSElement`)
- **Package assignments:** `AIXM_DataTypes.xsd` → `com.algonents.aixm.datatypes`, `AIXM_BasicMessage.xsd` → `com.algonents.aixm.message`, `gml.xsd` → `com.algonents.gml`

## Composite Build Usage

This module is referenced by air-app via `includeBuild("../aixm")` in `settings.gradle.kts`:

```
~/Repos/
├── air/    # Consumer (air-app)
├── aixm/   # This repository
└── skyui/  # UI component library
```

## Code Conventions

- Kotlin official code style (`kotlin.code.style=official`)
- Commit messages prefixed with category (e.g., `prog:`, `fix:`)
