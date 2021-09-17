name: Bug Report
description: Create a report for bugs, crashes, and other unintended behavior
title: "[Bug]: "
labels: ["bug", "triage"]
assignees:
 - TheIllusiveC4
- type: markdown
    attributes:
      value: |
        Hello, and thanks for filling out this bug report. Please respond to the following questions to the best of your ability.
- type: dropdown
    id: mc-version
    attributes:
      label: Minecraft Version
      description: What version of Minecraft are you running? (Unlisted versions are unsupported)
      options:
        - 1.17.1
        - 1.16.5
    validations:
      required: true
- type: textarea
    id: what-happened
    attributes:
      label: What happened?
      description: Also tell us, what did you expect to happen?
      placeholder: Explain what you've observed.
    validations:
      required: true
- type: textarea
    id: reproduction-steps
    attributes:
      label: How do you trigger this bug?
      description: Please make sure to remove all unnecessary mods first.
      placeholder: Walk through it step by step.
    validations:
      required: true
- type: dropdown
    id: loader
    attributes:
      label: Loader
      description: Are you using Forge or Fabric?
      options:
        - Forge
        - Fabric
    validations:
      required: true
- type: input
    id: loader-version
    attributes:
      label: Loader Version
      description: Which version of the above loader are you using? If using Fabric, also include the Fabric API version if applicable.
      placeholder: Enter the loader version.
    validations:
      required: true
- type: input
    id: mod-version
    attributes:
      label: Mod Version
      description: What version of the mod are you using?
      placeholder: Enter the mod version.
    validations:
      required: true
