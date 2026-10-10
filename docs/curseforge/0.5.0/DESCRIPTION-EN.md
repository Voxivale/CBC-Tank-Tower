![CBC Tank Tower 0.5](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/banner-en.png)

**MINECRAFT 1.21.1 · NEOFORGE · CREATE BIG CANNONS · 0.5.0**

CBC Tank Tower is a Create Big Cannons addon for compact tank construction. Build a glued turret, control it from one drive shaft, set safe firing arcs and automate ammunition handling. Adjustable running gear and a T-shaped gearbox help fit the drivetrain to your hull.

![Choose the mount for your hull](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/mount-family-en.png)

## Choose the mount for your hull

| Mount | Construction | Control |
| --- | --- | --- |
| Tank Cannon Mount | Glued turret body | Separate kinetic axes |
| Compact Tank Cannon Mount | Glued turret body | One shaft + redstone |
| Low Cannon Mount | Standard CBC gun | Lower pivot + rotating base |

![Tank Cannon Mount](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/turret-en.png)

## Tank Cannon Mount

**Armor turns. The barrel elevates.**

Glue armor and other turret blocks to the cannon with Create Super Glue or Aeronautics Honey Glue. The body follows horizontal aim while the barrel elevates independently.

- Attached body blocks do not change the cannon’s block count, stress, rotation coefficient or physical elevation limits.
- Keeps CBC assembly, firing and ammunition handling. Supports big cannons and autocannons, upright or inverted.
- Turret blocks, stored contents and glue joints survive saving and disassembly.

![Compact Tank Cannon Mount](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/compact-en.png)

## Compact Tank Cannon Mount

**One shaft. Redstone control.**

A turret mount with the same glued body support and a single kinetic input on the firing side. Route one shaft to the mount and control both aiming axes with redstone.

- Front: shaft input and firing signal. Rear: assemble or disassemble.
- Top / bottom: raise / lower the barrel. Right / left, relative to the original barrel direction: clockwise / counterclockwise traverse.
- Opposing commands stop their axis. Both axes can move together; shaft RPM controls aiming speed.
- For a shared drive and firing connection, use an Andesite Encased Shaft and apply the firing signal to its casing.

![Low Cannon Mount](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/low-en.png)

## Low Cannon Mount

**The cannon sits directly above the base.**

A lower version of the standard CBC mount: the base and cannon occupy two blocks in height. It retains separate kinetic aiming inputs and CBC redstone assembly and firing.

- Rotate the base with a Create wrench while keeping the upper gun and its aim in place.
- Supports upright and inverted placement.
- Uses standard CBC mounting behavior; it does not collect a glued turret body.

![Cannon traverse limits](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/limits-en.png)

## Cannon traverse limits

**Set the arc. Keep the gun inside it.**

Sneak and right-click any of the three mounts with an empty main hand to open its aiming limits. Two blueprint projections show the actual cannon from the side and from above.

- Four sliders set upward, downward, left and right limits relative to the original gun direction.
- Apply updates an assembled cannon immediately and saves the limits with the mount. Reset previews defaults; Cancel discards edits.
- The cannon’s own CBC elevation limits still apply. Left and right at 180° allow a full circle; both at 0° lock traverse.

![Automatic Loader](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/loader-en.png)

## Automatic Loader

**Select. Place. Power. Reload.**

A static brass block driven by a shaft on its round side port. Hold it before placement and right-click ammunition stores and target mounts, then place it within the configured Create mechanical arm range.

- Works with all three CBC Tank Tower mounts. Choose Round Robin, Forced Round Robin or Prefer First.
- Big cannons require a CBC Quickfiring Breech: the loader feeds a projectile, then a charged Big Cartridge. Autocannons receive native CBC magazines.
- Returns empty cartridges and magazines to the selected stores. If they are full, it holds the item and waits.
- Redstone pauses new intake. Native CBC ammunition checks and reload delays remain in force.

![Axle Gearbox](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/gearbox-en.png)

## Axle Gearbox

**One input. Two matching outputs.**

A T-shaped variant of Compact Transmission’s simple gearbox. Its two opposite outputs share the same signed rotation speed on their common axis.

- Horizontal shaft connections when placed on a floor or ceiling; turn the block with a Create wrench.
- Three ratios, redstone selection, reverse and neutral remain available.
- This block is enabled by Create Compact Transmission.

![Universal Wheel Mount](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/wheel-en.png)

## Universal Wheel Mount

**Set the stance. Set the wheelbase.**

An Offroad wheel mount with inward / outward and fore / aft positioning in 1/8-block steps. Fit the wheel to your hull while retaining native tires, steering and suspension.

- Inward / outward: −1.5 to +1 block. Fore / aft: −1 to +1.5 blocks.
- The spring, telescopic arm and lower mount move with the wheel. Rendering and physical contact use the same offsets.
- Right-click the top or rear shaft face with an empty hand, adjust the position and press Apply.

![Universal Track Mount](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/track-en.png)

## Universal Track Mount

**Fit the tracks to the hull.**

A Tracks+ mount with inward / outward positioning from −1 to +1 block in 1/8-block steps. Connected mounts share the native width adjustment, keeping the belt in one plane.

- Supports all six native roller and drive-wheel variants, belt coloring and suspension settings.
- Suspension moves with the tracks, including empty mounts and all suspension roller sizes.
- Right-click the top or rear shaft face with an empty hand, adjust the width and press Apply.

## Installation & compatibility

Minecraft 1.21.1 · NeoForge. Install CBC Tank Tower and matching dependencies on both the server and every client.

| Mod | Tested version | Purpose |
| --- | --- | --- |
| Create | 6.0.10 | Required |
| Create Big Cannons | 5.11.7 | Required |
| Create Compact Transmission | 1.0.7 | Optional · Axle Gearbox |
| Aeronautics / Offroad + Tracks+ | 1.3.2 + 1.0.5 | Optional · both universal mounts |
| Aeronautics / Simulated | 1.3.2 | Optional · Honey Glue |

The three cannon mounts, their aiming interface and the loader work with Create and CBC. Running gear is enabled when both Offroad and Tracks+ are installed. Include each dependency’s own required libraries. Tested on NeoForge 21.1.252. VoxHole / CreateHole is not required.

## More to come

CBC Tank Tower aims to make tank building more compact. More practical construction features are planned for future updates.

[Source on GitHub](https://github.com/Voxivale/CBC-Tank-Tower)
