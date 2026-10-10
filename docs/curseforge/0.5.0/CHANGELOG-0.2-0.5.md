# CBC Tank Tower — 0.2–0.5
**Minecraft 1.21.1 · NeoForge**

## 0.5.0 — Cannon aiming limits

- Added an aiming limits interface to the Tank Cannon Mount, Compact Tank Cannon Mount and Low Cannon Mount. Sneak and right-click the base with an empty main hand to open it.
- Added side and top blueprint projections of the actual mounted cannon, plus separate sliders for upward, downward, left and right limits.
- Limits use the original gun direction as zero. Left and right at 180° permit full traverse; zero on both sides locks it. Native CBC physical elevation limits remain in force.
- Apply updates assembled cannons immediately. Settings persist with the mount and synchronize between server and client; existing mounts keep their default freedom of movement.
- Prevented high-speed aiming from skipping stops. Rotating the low mount’s base does not move its firing sector.
- Added Reset, Cancel and Apply controls, English and Russian localization, and layouts for smaller windows and empty mounts.

## 0.4.1 — Independent low mount base

- The Low Cannon Mount base can now be rotated with a Create wrench without rotating the upper gun, changing its aim or reassembling it.
- Shaft ports and redstone connections follow the base orientation. The upper mount keeps its original orientation.
- Base rotation is saved with the block and supports structure rotation and mirroring. Existing mounts use their original base orientation.

## 0.4.0 — Automatic Loader

- Added the Automatic Loader: a static brass block with one side shaft input, using Create mechanical arm speed and stress behavior.
- Before placing it, select ammunition stores and target mounts. All three CBC Tank Tower mounts are supported; sources preserve the selected inventory side.
- Added Round Robin, Forced Round Robin and Prefer First modes. Redstone pauses new intake.
- Big cannons with a Quickfiring Breech receive a projectile followed by a charged Big Cartridge; autocannons receive CBC magazines. CBC acceptance rules and reload delays are preserved.
- Empty cartridges and magazines return to selected stores. A full store pauses the transfer safely; buffered items and selected points survive saving.
- Added the recipe, localization and support for relative points on Sable / Aeronautics constructions.
- Fixed the optional dependency condition in the Axle Gearbox loot table.

## 0.3.0 — Compact and low cannon mounts

- Added the Compact Tank Cannon Mount with glued turret body support and one kinetic input on the firing side.
- Redstone controls elevation from the top and bottom, traverse from the sides, assembly from the rear and firing from the drive side. Opposing signals stop their axis; both axes can move simultaneously.
- Added the Low Cannon Mount: the cannon pivot sits one block above the base, or below it when inverted. It keeps standard CBC controls and does not collect a glued turret body.
- Added recipes and localization for both mounts. Saving and disassembly preserve their correct pivot positions.

## 0.2.2 — Suspension follows running gear

- Wheel positioning now moves the spring, telescopic arm and lower mount together with the tire, including fore / aft adjustments.
- Track width changes now move the suspension with the belt and rollers, including empty mounts and every suspension roller size.
- Preserved native steering, suspension motion, wheel / track appearance and matching physical contact offsets.

## 0.2.1 — Horizontal gearbox connections

- Corrected Axle Gearbox placement so floor and ceiling installation produces a horizontal T-shaped shaft arrangement.
- Updated the block’s port appearance and orientation to match its actual shaft connections. Create wrench rotation remains available.

## 0.2.0 — Transmission and adjustable running gear

- Added the Axle Gearbox, a T-shaped Compact Transmission simple gearbox variant with two opposite outputs sharing the same signed speed on their common axis.
- Added the Universal Wheel Mount based on Offroad, with inward / outward and fore / aft offsets in 1/8-block steps.
- Added the Universal Track Mount based on Tracks+, with inward / outward offsets in 1/8-block steps and support for connected track width adjustment.
- Added configuration screens, server synchronization, recipes and English / Russian localization for the new parts.
- Kept the integrations optional: Compact Transmission enables the gearbox; Offroad together with Tracks+ enables the universal mounts. Core cannon mounts still work with Create and CBC.


---

# Русский

# CBC Tank Tower — 0.2–0.5
**Minecraft 1.21.1 · NeoForge**

## 0.5.0 — Ограничения наведения

- Добавлен интерфейс ограничений для башенного, компактного башенного и низкого креплений. Открывается сочетанием Shift + ПКМ по основанию с пустой основной рукой.
- Добавлены чертёжные проекции установленной пушки сбоку и сверху, а также отдельные ползунки пределов вверх, вниз, влево и вправо.
- Нулевое направление соответствует исходному положению ствола. По 180° слева и справа разрешают полный круг, два нулевых значения блокируют поворот. Физические пределы наклона CBC сохраняются.
- Ограничения применяются к собранной пушке сразу, сохраняются в креплении и синхронизируются между сервером и клиентом. Старые крепления сохраняют стандартную свободу наведения.
- Высокие обороты больше не позволяют перескочить через ограничитель. Поворот основания низкого крепления не сдвигает сектор пушки.
- Добавлены кнопки сброса, отмены и применения, русская и английская локализация, поддержка небольших окон и настройки пустых креплений.

## 0.4.1 — Независимый поворот основания

- Основание низкого крепления теперь можно повернуть ключом Create без поворота верхней части, изменения наведения и пересборки пушки.
- Входы валов и редстоунные подключения следуют повороту основания, верхнее крепление сохраняет исходную ориентацию.
- Положение основания сохраняется в мире и поддерживает поворот и зеркальное отражение конструкций. Старые крепления используют исходное положение основания.

## 0.4.0 — Автоматический заряжатель

- Добавлен автоматический заряжатель — статичный латунный блок с одним боковым входом вала, скоростью и нагрузкой механической руки Create.
- Склады боеприпасов и целевые крепления выбираются до установки. Поддерживаются все три крепления CBC Tank Tower; учитывается выбранная сторона склада.
- Добавлены режимы Round Robin, Forced Round Robin и Prefer First. Редстоун приостанавливает новый забор предметов.
- Большие пушки со скорострельным казёнником Quickfiring Breech получают сначала снаряд, затем заряженную большую гильзу. Автопушки получают магазины CBC. Сохраняются штатные проверки и задержки заряжания.
- Пустые гильзы и магазины возвращаются в отмеченные склады. При нехватке места перенос безопасно останавливается. Предмет в переносе и выбранные точки сохраняются.
- Добавлены рецепт, локализация и поддержка относительных точек на конструкциях Sable / Aeronautics.
- Исправлено условие необязательной зависимости в таблице добычи мостовой коробки передач.

## 0.3.0 — Компактное и низкое крепления

- Добавлено компактное башенное крепление с поддержкой приклеенного корпуса и единственным входом вращения на стороне выстрела.
- Редстоун сверху и снизу управляет наклоном ствола, по бокам — поворотом башни, сзади — сборкой, со стороны вала — выстрелом. Встречные сигналы останавливают свою ось, обе оси могут двигаться одновременно.
- Добавлено низкое крепление: ось пушки находится на один блок выше основания, а при перевёрнутой установке — ниже. Сохраняет обычное управление CBC и не собирает приклеенный корпус башни.
- Добавлены рецепты и локализация двух креплений. Сохранение и разборка учитывают их высоту оси.

## 0.2.2 — Подвеска следует за ходовой

- Регулировка колёс теперь перемещает пружину, телескопический рычаг и нижнее крепление вместе с шиной, в том числе при смещении вперёд и назад.
- При изменении ширины гусениц подвеска перемещается вместе с лентой и роликами, включая пустые крепления и все размеры подвесных роликов.
- Сохранены штатное рулевое управление, работа подвески, внешний вид колёс и гусениц, а также соответствие смещений физическим точкам контакта.

## 0.2.1 — Горизонтальные подключения коробки

- Исправлено расположение валов мостовой коробки передач: при установке на пол или потолок подключения образуют горизонтальную букву T.
- Внешний вид отверстий и ориентация блока приведены в соответствие с подключениями валов. Сохранён поворот ключом Create.

## 0.2.0 — Трансмиссия и регулируемая ходовая

- Добавлена мостовая коробка передач — T-образный вариант простой коробки Compact Transmission с одинаковой знаковой скоростью двух противоположных выходов на общей оси.
- Добавлено универсальное колёсное крепление на основе Offroad со смещениями внутрь / наружу и вперёд / назад с шагом 1/8 блока.
- Добавлено универсальное гусеничное крепление на основе Tracks+ со смещением внутрь / наружу с шагом 1/8 блока и общей регулировкой ширины соединённых креплений.
- Добавлены экраны настройки, синхронизация с сервером, рецепты и русская / английская локализация новых деталей.
- Интеграции остались необязательными: Compact Transmission включает коробку передач, Offroad вместе с Tracks+ — универсальные крепления. Основные крепления пушек работают с Create и CBC.
