# MultiMiner

A Fabric mod that adds vein mining and tree felling to Minecraft, with a fully configurable in-game settings menu.

## Features

- **Vein Mining** — Break connected ore veins in one action
- **Tree Felling** — Chop entire trees by breaking one log
- **Config Menu** — In-game configuration via Mod Menu integration

## Project Structure

```
MultiMiner/
├── docs/               # Documentation
│   ├── README.md
│   ├── TODO.md
│   ├── CHANGELOG.md
│   └── MODRINTH.md     # Modrinth page description
├── 1.19.2/             # Fabric mod for MC 1.19.2
├── 1.19.4/             # Fabric mod for MC 1.19.4
├── 1.21.1/             # Fabric mod for MC 1.21.1
├── 1.21.2/             # Fabric mod for MC 1.21.2
├── 1.21.4/             # Fabric mod for MC 1.21.4
├── 1.21.6/             # Fabric mod for MC 1.21.6
├── 1.21.8/             # Fabric mod for MC 1.21.8
├── 1.21.9/             # Fabric mod for MC 1.21.9
└── 1.21.11/            # Fabric mod for MC 1.21.11
```

Each version folder contains a standalone Fabric mod project targeting that Minecraft version.

## Dependencies

- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Mod Menu](https://modrinth.com/mod/modmenu) (optional, for config screen)

## Building

Each version is an independent Gradle project. To build:

```bash
cd <version>/
./gradlew build
```

Output jar will be in `<version>/build/libs/`.

## License

MIT
