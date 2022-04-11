# Cup of Java ![cup](https://raw.githubusercontent.com/Denis-spec989/DifferentPhotos/master/Hakaton/cupofjava1.png)
## **Полуфинал BEST Hack '22**
## Направление **Java Backend**
## Участники:
+ ### Борисовская Алена Алексеевна ([Alienstemple](https://github.com/Alienstemple)) (капитан)
+ ### Игнатов Денис Сергеевич ([Denis-spec989](https://github.com/Denis-spec989))
+ ### Циндяйкин Вадим Денисович ([Vadim-Burns](https://github.com/Vadim-Burns))
+ ### Чихутова Дарья Денисовна ([dashachickutova](https://github.com/dashachickutova))

---

## Причина ошибки:
При запуске программы происходит вызов метода `SenderService.sendTestMessages()`, который отправляет в очередь 80 сообщений с одинаковым полем `name`:
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

Сообщения, которые выходят из очереди обрабатываются методом 
`AmqpListener.hakatonDemoQueue(String message)`:

```java
@RabbitListener(queues = "hakatonDemoQueue")
private void hakatonDemoQueue(String message) 
{ domainServicesaveHakatonEntity(new SendingDto(message)); }
```

Этот метод создает объект `new SendingDto(message)` и передает его в метод `DomainService.saveHakatonEntity(...)`. Единственное поле `name` в классе-сущности `HakatonEntity` помечено аннотацией `@Id` , которое помечает поле в классе модели как первичный ключ и запрещает дубликаты:

```java
@Id
private String name;
```

 Метод `DomainService.createHakatonEntityIfNotExists(...)` добавляет наш объект в БД, если таких не было ранее.

```Java
 @Override
    public void saveHakatonEntity(SendingDto sendingDto) {
        hakatonEntityRepository.createHakatonEntityIfNotExists(sendingDto.getName());
    }
}
```

Однако:scream_cat:, вследствие **параллельного выполнения нескольких аналогичных процессов**, при добавлении одинаковых записей, возникает ситуация **конкурентного доступа** (race condition): между проверкой отсутствия объекта и записью нового объекта в параллельном потоке происходит запись объекта с таким же идентификатором, что приводит к аномалии **фантомного чтения** (phantom read). Таким образом во время проверки конфликтующей строки в таблице нет, поэтому далее происходит попытка добавления строки, но в момент добавления строка с таким идентификатором уже существует в таблице. Это приводит к выбрасыванию ошибки:no_entry::

```Sql
org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint "hakaton_entity_pk""
```
---
<a id="anchor1"></a>
## Метод устранения ошибки :white_check_mark:
Наше решение затрагивает функцию записи объекта в бд, а именно один из наших вариантов решения это использование nativeQuery, чтобы особенности Postgresql решали задачу за нас:
```Java
    @Modifying
    @Transactional
    @Query(value = "insert into hakaton_entity (name) values (?) ON CONFLICT DO NOTHING", nativeQuery = true)
    void createHakatonEntityIfNotExists(String name);
}
```
Мы  шаблонизируем запрос к БД через аннотацию `@Query`,используя чистый SQL(nativeQuery= true) для использования `insert and ON CONFLICT DO NOTHING`.
Аннотация `@Modifying` используется для расширения функционала `@Query`, добавляя возможность использования команд `INSERT,UPDATE,DELETE`. Аннотация `@Transactional` оборачивает наш процесс в отдельную транзакцию, если такой не существовало ранее.

---
## Вывод
Нами были рассмотрены еще 1 возможный варианта решения:

:shipit: Синхронизация
```Java
@Override
    public synchronized void saveHakatonEntity(SendingDto sendingDto) {
        if(!hakatonEntityRepository.existsHakatonEntityByName(sendingDto.getName())){
            hakatonEntityRepository.save(new HakatonEntity(sendingDto.getName()));
        }
    }
```
Однако, мы нашли более оптимальное решение - описанное [выше](#anchor1).





