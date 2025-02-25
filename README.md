# MineQuery

Some utilities for vanilla survival.

To old users, MineQuery have moved on and is now available on both Fabric and NeoForge by Architectury.

## FAQ

### About the Legacies?

To my best users who chased MineQuery till now:

From now on, MineQuery will support both Fabric and NeoForge using Architectury.
So the old versions on Fabric are now deprecated.
You can still use them for older Minecraft versions (1.16–1.20), but they are not going to receive any updates.

- [1.17 - 1.20](https://github.com/Taskeren/mineQuery-1.17)
- [1.16](https://github.com/Taskeren/mineQuery-legacy)

And one more thing is that the public license is changed from CC0 to MIT.

### Can I Include MineQuery in my Modpack?

Yes, you are still allowed to include this in your modpacks without my permissions.

### I want a new Feature!

I'm looking forward to hearing your advice.
You can open a new issue with your detailed requests about the functionalities.
I'll look into it to check if it is possible to come true.

But it is not guaranteed!

### I found an Unexpected Behavior?

Of course, there are bugs.
You can open a new issue with the detailed information about it, especially the reproducing steps.
I'll look into it to make a fix.

### DISCLAIMER!

This is a **Client-Only** mod, so you don't need to have it on the server.

However, you must know that MineQuery sends packets that are not triggered by players, which may violate the rules of
some servers that MineQuery can be considered as bot or cheating mod.

**USE AT YOUR OWN RISKS.**

## Features

### Harvest X

Harvest X prevents you from unintentionally breaking your crops, and helps you to replant the crops when harvesting.

There are two modes, "left-clicking mode" and "right-clicking mode":

#### Right-clicking Mode (Recommended)

The right-clicking mode means Harvest X will send a break action to the server when you right-click mature crops.
As you've already pressed right-click, you'll replant with seeds in your hand.

This mode is more recommended because it looks more like a human action.

#### Left-clicking Mode

The left-clicking mode means Harvest X will prevent you from breaking immature crops (e.g., wheat, carrots, potatoes;
stems are not included).
And will send a right-click action of your main hand to the server when you successfully harvest a crop.

#### Non-Crops

For non-crops, there are few more rules on them.

##### Stems

Stems are not breakable at any time. Because their harvest is not the stem itself.

##### Cactus / Sugar Cane

Cacti and Sugar Canes on the bottom are not breakable at any time.

### NotAttack

NotAttack prevents you from *directly* hurting Villagers and Iron Golems.

But notice that there is still a chance to hurt them because of **Sweeping Edge**.

### NotPlace

NotPlace prevents you from placing blocks on unexpected faces.

It is useful when you place a big platform in SkyBlock.

You can disable the faces by Shift-right-clicking on the side with a stick named `NotPlace` (case-insensitive).
Also, that stick will have an additional tooltip to show which sides are NotPlace-d.

### Command Shortcuts

Being tired of typing *LOOOOOONG* commands in the chat? Command Shortcuts comes!

You can make commands binding to a key in the *Command Shortcuts Config Screen* by hitting <kbd>J</kbd>.

You need to put the commands in the left text field, and press *Set* button to save it.
And you need to assign a key on the right button, like the key binding screen in the options.
Unassign by assigning with <kbd>Esc</kbd>.

The command need to be start with slash (`/`), otherwise, it is treated as a text.

The size of shortcuts can be configured in the configuration, but it requires restart.
