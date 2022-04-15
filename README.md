# Java Backend Полуфинал BEST Hack '22
# Команда "Кодеры вроде бы"
 - ### Овсянников Александр (капитан) (https://vk.com/a.ovsyannikov1)
 - ### Строева Ольга (https://vk.com/id203342031)
 - ### Козявин Максим (https://vk.com/kanzu32)
 - ### Сафронова Анастасия (https://vk.com/astas_konop)
## Описание проблемы
При запуске программы вызывается метод sendTestMessages():
```java
 public void sendTestMessages() throws JsonProcessingException {
        byte[] array = new byte[14];
        new Random().nextBytes(array);
        String name = DigestUtils.md5DigestAsHex(array);

        for (int i = 0; i < 80; i++) {
            rabbitTemplate.convertAndSend("hakatonDemoQueue",name);
        }
    }
```
Этот метод отправлет 80 сообщений с одинаковым полем name
Сообщения обрабатываются методом AmqpListener.hakatonDemoQueue(String message):
```java
@RabbitListener(queues = "hakatonDemoQueue")
    private void hakatonDemoQueue(String message) {
        domainService.saveHakatonEntity(new SendingDto(message));
    }
```
Данный метод передает объект типа SendingDto в метод saveHakatonEntity(SendingDto sendingDto):
```java
public void saveHakatonEntity(SendingDto sendingDto) {
        if(!hakatonEntityRepository.existsHakatonEntityByName(sendingDto.getName())){
            hakatonEntityRepository.save(new HakatonEntity(sendingDto.getName()));
        }
    }
```
Передаётся одно поле name в конструктор. Поле является идентификатором (ключом записи).
В методе saveHakatonEntity есть проверка на уникальность этого поля, но из-за выполнения нескольких паралелльных потоков возникает ошибка "ERROR: duplicate key value violates unique constraint "hakaton_entity_pkey""
т.е. несколько потоков с одниковым идентификатором name выполняются параллельно и первый поток записывает запись в бд, а следующие не могут записать, потому что СУБД не позволяет этого сделать в следствии чего возникает ошибка.
## Решение проблемы
Решением нашей команды является использование синхронизации потоков, чтобы несколько потоков с одиновым идентификатором не могли получить доступ к функции saveHakatonEntity(SendingDto sendingDto) одновременно.
Для этого мы используем ключевое слово synchronized:
```java
    synchronized public void saveHakatonEntity(SendingDto sendingDto) {
        if(!hakatonEntityRepository.existsHakatonEntityByName(sendingDto.getName())){
            hakatonEntityRepository.save(new HakatonEntity(sendingDto.getName()));
        }
    }
```
Данное решение исправляет ошибку.
