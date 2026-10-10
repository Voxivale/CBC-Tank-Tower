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

---

# Русское описание

![CBC Tank Tower 0.5](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/banner-ru.png)

**MINECRAFT 1.21.1 · NEOFORGE · CREATE BIG CANNONS · 0.5.0**

CBC Tank Tower — аддон для Create Big Cannons, который делает танкостроение компактнее. Собирайте приклеенную башню, управляйте ею от одного вала, задавайте сектор наведения и автоматизируйте заряжание. Регулируемая ходовая и T-образная коробка передач помогут подогнать механику под корпус.

![Выберите крепление под свой корпус](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/mount-family-ru.png)

## Выберите крепление под свой корпус

| Крепление | Конструкция | Управление |
| --- | --- | --- |
| Башенное | Приклеенный корпус башни | Отдельные кинетические оси |
| Компактное башенное | Приклеенный корпус башни | Один вал и редстоун |
| Низкое | Обычная пушка CBC | Низкая ось и поворот основания |

![Башенное крепление пушки](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/turret-ru.png)

## Башенное крепление пушки

**Броня поворачивается. Ствол поднимается.**

Приклейте броню и другие блоки башни к пушке суперклеем Create или медоклеем Aeronautics. Корпус следует горизонтальному наведению, а ствол поднимается и опускается отдельно.

- Приклеенный корпус не меняет число блоков пушки, её нагрузку, коэффициент вращения и физические пределы наклона.
- Сохраняются сборка, стрельба и работа с боеприпасами CBC. Поддерживаются большие пушки, автопушки и перевёрнутая установка.
- Блоки башни, содержимое контейнеров и клеевые соединения сохраняются при сохранении мира и разборке.

![Компактное башенное крепление](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/compact-ru.png)

## Компактное башенное крепление

**Один вал. Управление редстоуном.**

Башенное крепление с тем же сбором приклеенного корпуса и единственным входом вращения на стороне выстрела. Подведите один вал и управляйте обеими осями редстоуном.

- Спереди — вал и сигнал выстрела. Сзади — сборка и разборка.
- Сверху / снизу — поднять / опустить ствол. Справа / слева относительно исходного направления ствола — поворот по часовой / против часовой стрелки.
- Встречные сигналы останавливают свою ось. Обе оси могут двигаться одновременно; скорость задаётся оборотами вала.
- Для подачи вращения и выстрела через одну грань используйте вал в андезитовом корпусе Create с сигналом на корпусе.

![Низкое крепление пушки](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/low-ru.png)

## Низкое крепление пушки

**Пушка сразу над основанием.**

Укороченный вариант обычного крепления CBC: основание и пушка занимают два блока по высоте. Сохраняются отдельные кинетические входы наведения, редстоунная сборка и стрельба.

- Поворачивайте основание ключом Create: верхняя часть пушки и её наведение остаются на месте.
- Поддерживаются обычная и перевёрнутая установки.
- Работает как обычное крепление CBC и не собирает приклеенный корпус башни.

![Ограничения наведения](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/limits-ru.png)

## Ограничения наведения

**Задайте сектор движения пушки.**

Зажмите Shift и нажмите ПКМ по любому из трёх креплений с пустой основной рукой. В интерфейсе показаны две чертёжные проекции настоящей пушки — сбоку и сверху.

- Четыре ползунка задают пределы вверх, вниз, влево и вправо относительно исходного направления ствола.
- «Применить» сразу обновляет собранную пушку и сохраняет ограничения. «Сбросить» показывает значения по умолчанию, «Отмена» закрывает меню без изменений.
- Штатные физические пределы CBC сохраняются. По 180° влево и вправо дают полный круг; два нулевых значения блокируют горизонтальный поворот.

![Автоматический заряжатель](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/loader-ru.png)

## Автоматический заряжатель

**Отметьте. Установите. Подведите вращение.**

Статичный латунный блок с входом вала на круглой боковой грани. До установки отметьте ПКМ склады боеприпасов и целевые крепления, затем поставьте заряжатель в пределах дальности механической руки Create.

- Работает со всеми тремя креплениями CBC Tank Tower. Доступны Round Robin, Forced Round Robin и Prefer First.
- Для больших пушек нужен скорострельный казённик CBC — Quickfiring Breech: сначала подаётся снаряд, затем заряженная большая гильза. Для автопушек используются магазины CBC.
- Возвращает пустые гильзы и магазины в отмеченные склады. Если места нет, удерживает предмет и ждёт.
- Редстоун приостанавливает новый забор. Проверки боеприпасов и задержки заряжания остаются штатными для CBC.

![Мостовая коробка передач](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/gearbox-ru.png)

## Мостовая коробка передач

**Один вход. Два согласованных выхода.**

T-образный вариант простой коробки передач Compact Transmission. Два противоположных выхода имеют одинаковую знаковую скорость вращения на общей оси.

- При установке на пол или потолок отверстия валов расположены горизонтально. Ориентация меняется ключом Create.
- Сохраняются три передаточных числа, выбор редстоуном, реверс и нейтраль.
- Этот блок появляется при установленном Create Compact Transmission.

![Универсальное колёсное крепление](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/wheel-ru.png)

## Универсальное колёсное крепление

**Настройте колею и колёсную базу.**

Крепление Offroad с перемещением колеса внутрь / наружу и вперёд / назад с шагом 1/8 блока. Подгоняйте колёса под корпус, сохраняя штатные шины, рулевое управление и подвеску.

- Поперёк корпуса: от −1,5 до +1 блока. Вдоль: от −1 до +1,5 блока.
- Пружина, телескопический рычаг и нижнее крепление перемещаются вместе с колесом. Модель и физическая точка контакта используют одинаковые смещения.
- ПКМ пустой рукой по верхней или задней стороне подключения вала открывает настройку. Для сохранения нажмите «Применить».

![Универсальное гусеничное крепление](https://raw.githubusercontent.com/Voxivale/CBC-Tank-Tower/main/docs/curseforge/0.5.0/images/track-ru.png)

## Универсальное гусеничное крепление

**Подгоните гусеницы под корпус.**

Крепление Tracks+ со смещением внутрь / наружу от −1 до +1 блока, шаг — 1/8 блока. Соединённые крепления получают общее изменение ширины, сохраняя ленту в одной плоскости.

- Поддерживаются все шесть штатных вариантов роликов и ведущих колёс, окраска ленты и настройки подвески.
- Подвеска перемещается вместе с гусеницей, включая пустые крепления и все размеры подвесных роликов.
- ПКМ пустой рукой по верхней или задней стороне подключения вала открывает ширину. Для сохранения нажмите «Применить».

## Установка и совместимость

Minecraft 1.21.1 · NeoForge. Одинаковая версия CBC Tank Tower и подходящие зависимости нужны на сервере и у всех игроков.

| Мод | Проверенная версия | Назначение |
| --- | --- | --- |
| Create | 6.0.10 | Обязательно |
| Create Big Cannons | 5.11.7 | Обязательно |
| Create Compact Transmission | 1.0.7 | Опционально · коробка передач |
| Aeronautics / Offroad + Tracks+ | 1.3.2 + 1.0.5 | Опционально · оба крепления ходовой |
| Aeronautics / Simulated | 1.3.2 | Опционально · медоклей |

Три крепления пушек, интерфейс углов и заряжатель работают с Create и CBC. Ходовая появляется при одновременной установке Offroad и Tracks+. Добавьте также штатные библиотеки зависимостей. Проверено на NeoForge 21.1.252. VoxHole / CreateHole не требуется.

## Дальше — больше

CBC Tank Tower стремится сделать танкостроение компактнее. В будущих обновлениях появятся новые возможности для строительства техники.

[Исходный код на GitHub](https://github.com/Voxivale/CBC-Tank-Tower)
