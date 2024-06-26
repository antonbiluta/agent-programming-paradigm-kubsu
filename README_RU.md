### Доступные языки

- [EN](README.md)

# Мультиагентные системы на платформе JADE

## Описание

Этот репозиторий содержит четыре лабораторные работы,
выполненные с использованием платформы JADE для создания
мультиагентных систем.
Каждая работа решает конкретную задачу
в разных предметных областях.

## Содержание

1. Ping Pong
2. Торговля книгами
3. Размещение заказов на оборудование
4. Управление складом

## 1. Ping Pong

### Описание

В этой лабораторной работе реализована простая
мультиагентная система, в которой два агента
обмениваются сообщениями, имитируя игру в пинг-понг.
Каждый агент отправляет сообщение "Ping" или "Pong" и
ждет ответа от другого агента.

### Файлы

- `PingAgent.java`
- `PongAgent.java`
- `MainLauncher.java`

### Запуск

1. Запустите `MainLauncher` для создания и запуска агентов Ping и Pong.
2. Наблюдайте за обменом сообщениями в консоли.

## 2. Торговля книгами

### Описание

Эта лабораторная работа моделирует процесс торговли
книгами между покупателями и продавцами.
Агенты покупателей отправляют запросы на покупку книг,
а агенты продавцов отвечают на эти запросы с предложениями.
Покупатели выбирают наилучшие предложения и совершают покупки.

### Файлы
- `BookBuyerAgent.java`
- `BookSellerAgent.java`
- `MainLauncher.java`

### Запуск

- Запустите `MainLauncher` для создания и запуска агентов покупателей и продавцов.
- Наблюдайте за процессом торговли книгами в консоли.

## 3. Размещение заказов на оборудование

### Описание

В этой лабораторной работе моделируется процесс выбора
оборудования для фирм на основе их требований.
Агенты фирм отправляют запросы производителям,
получают предложения и выбирают наилучшие предложения.

### Файлы

- `AgentCompany.java`
- `AgentManufacturer.java`
- `MainLauncher.java`

### Запуск

- Запустите MainLauncher для создания и запуска агентов фирм и производителей.
- Наблюдайте за процессом выбора оборудования в консоли.

## 4. Управление складом

### Описание

Эта лабораторная работа моделирует процесс управления складом.
Агенты получают заказы, комплектуют товары и управляют
складскими запасами. Агент-менеджер получает заказы,
агенты по сборке и доставке комплектуют и доставляют товары,
а агент-склад управляет инвентарем.

### Файлы

- `AgentWarehouseManager.kt`
- `AgentAssembler.kt`
- `AgentWarehouse.kt`
- `AgentExtensions.kt`
- `Utils.kt`
- `MainLauncher.kt`

### Запуск

- Запустите `MainLauncher` для создания и запуска агентов менеджера склада, сборщиков и склада.
- Наблюдайте за процессом управления складом в консоли.

## Требования

Для выполнения этих лабораторных работ необходима
установленная платформа JADE и JDK.

### Установка JADE

#### Вариант 1: Установка с [официального сайта JADE](http://jade.tilab.com/download/jade/)
#### Вариант 2: Использование форка JADE с [GitLab](https://gitlab.com/jade-project)

### Авторы

Лабораторные работы выполнил [Антон Билута](https://github.com/antonbiluta)
<br>под руководством к.т.н. доцента Приходько Т.А.