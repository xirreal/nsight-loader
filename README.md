# NSight Loader
### actually this loads Renderdoc too but whatever

This version-agnostic Fabric mod allows you to inject NSight or Renderdoc into Minecraft, allowing you to attach at any time instead of having to launch the game through the GUI either manually or by launching the launcher (wow that sucks to say).

### Usage

1. Install Fabric (1.18.2 or later, as long as the loader is 0.15.0 or newer)
2. Drop the mod into your mods folder
3. Launch the game, pick the debugger you want to use
4. Attach to the game process via your debugger of choice
5. Profit?

### Advanced usage

You can skip the dialog (useful for some dev environments) by setting the `debugging` flag to the debugger you want.
Allowed options are:
```shell
-Ddebugger=nsight-frame
-Ddebugger=nsight-gpu
-Ddebugger=renderdoc
```