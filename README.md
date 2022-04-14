# **Java Backend hackathon**

## **Команда: Nuclear Cats**

 - ### Корначук Марк Всеволодович ([PotatoHD404](https://github.com/PotatoHD404))
 - ### Лукичев Артемий Николаевич ([Ponchik-Lukich](https://github.com/Ponchik-Lukich))

---

## **Приложение**

### **Запуск**

Для запуска приложения необходима база данных PostgreSQL и
сервис отправки сообщений RabbitMQ. В проекте предусмотрен
автоматический запуск этих сервисов командой

```
docker-compose up
```

Проект уже сконфигурирован для подключения к данным сервисам.

### **Описание**

При запуске приложения происходит вызов метода `sendTestMessages`, который
80 раз отправляет одинаковое сообщение, содержащее случайное имя, приемнику.

```java
@Override
public void sendTestMessages() throws JsonProcessingException {
    byte[] array = new byte[14];
    new Random(System.nanoTime()).nextBytes(array);

    String name = DigestUtils.md5DigestAsHex(array);
    for (int i = 0; i < 80; i++) {
            rabbitTemplate.convertAndSend("hackathonDemoQueue",name);
    }
}
```

В программе запущено 25 приемников, которые получают сообщения и пишут их в очередь.
Сообщения из очереди параллельно обрабатываются методом `hackathonDemoQueue`.

```java
@RabbitListener(queues = "hackathonDemoQueue")
private void hackathonDemoQueue(String message) {
    domainService.saveHackathonEntity(new SendingDto(message));
}
```

Из полученной строки создается объект, который передается в метод `saveHackathonEntity`,
находящийся в `DomainService`.
`saveHackathonEntity`, в свою очередь проверяет, есть ли запись с таким именем в
базе данных, если нет, создает такую запись.

```java
@Override
public void saveHackathonEntity(SendingDto sendingDto) {
    if (!hackathonEntityRepository.existsHackathonEntityByName(sendingDto.getName())) {
        hackathonEntityRepository.save(new HackathonEntity(sendingDto.getName()));
    }
}
```

### **Ошибка**

Ошибка `PSQLException: ERROR: duplicate key value violates unique constraint.`
возникает в `saveHackathonEntity` в строке

```java
hackathonEntityRepository.save(new HackathonEntity(sendingDto.getName()));
```

в момент, когда `saveHackathonEntity` вызывается из нескольких потоков
одновременно. 

Из-за многопоточности проверка на то, что id, который мы пытаемся записать,
нет, срабатывает некорректно: несколько потоков одновременно запускают 
данную проверку и получают разрешение на запись. Однако база данных
не позволяет создать несколько строк с одинаковым id из-за чего только
первый поток выполняет код корректно, а все остальные выдают ошибку.
Так же было обнаружено, что запросы собираются в очередь, 
после чего параллельно обрабатываются по несколько штук
в один и тот же момент.

---

## **Постановка задачи**

В ходе выполнения задачи мы более четко сформулировали
техническое задание.

Существует уже написанный тестовый проект, работающий с RabbitMQ и PostgreSQL. При
запуске проекта возникает ошибка: 
`PSQLException: ERROR: duplicate key value violates unique constraint.`
Исправить возникшую ошибку, не изменяя структуру базы данных, чтобы 
код внутри `if` выполнялся только один раз.

```java
@Override
public void saveHackathonEntity(SendingDto sendingDto) {
    if (!hackathonEntityRepository.existsHackathonEntityByName(sendingDto.getName())) {
        hackathonEntityRepository.save(new HackathonEntity(sendingDto.getName()));
    }
}
```

### **Замечание**
Ошибка сформулирована таким образом, так как внутри `if` может находиться более сложная
логика, вследствие чего его многократный вызов недопустим, так как эта логика, вероятно,
будет работать некорректно. Убирать `if` нельзя по той же причине.

---

## **Исправление**

Исправить данную ошибку можно путем синхронизации потоков в момент
вызова функции записи так, чтобы не возникало параллельных
вызовов. Для этого стоит использовать ключевое слово
`synchronized`, позволяющее выполнять функцию, к которой
оно применено, в одном потоке.

---

## **Альтернативные реализации**

### **Транзакции**

Использовать декоратор `@Transactional`, однако,
оказалось, что он не синхронизирует потоки и может произойти,
та же ошибка. Возможна ситуация, при которой меняются сразу
несколько строк и проверяются несколько строк, что вызовет
ошибку даже на уровне изоляции `REPEATABLE_READ`. 
Значит следует использовать уровень изоляции `SERIALIZABLE`,
что фактически будет соответствать ключевому слову synchronized,
однако производительность будет меньше и программа, возможно, вызовет
ошибки в дальнейшем.

### **Игнорирование ошибки**

Отключить ошибку любым методом, например через `try` `catch` или
на уровне базы данных через `ON CONFLICT DO NOTHING`. Оба этих метода
плохи, так как вызывают лишние запросы, а кроме того противоречат нашей
постановке задачи: код внутри `if` срабатывает не один раз, что вызовет
проблемы при дальнейшей разработке.

### **Уменьшение количества приемников**

Убавив количества приемников до 1 можно избавиться от параллельных
вызовов функций, однако и скорость приложения при этом значительно
уменьшится.

---

## **Вывод**

Всё что нужно сделать - добавить
ключевое слово `synchronized` методу
`saveHackathonEntity` в классе DomainServiceImpl.

```java
@Override
public synchronized void saveHackathonEntity(SendingDto sendingDto) {
    if (!hackathonEntityRepository.existsHackathonEntityByName(sendingDto.getName())) {
        hackathonEntityRepository.save(new HackathonEntity(sendingDto.getName()));
    }
}
```


