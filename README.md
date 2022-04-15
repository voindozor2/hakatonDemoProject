# hakatonDemoProject
Решение команды IV задания хакатона
Участники команды: 
Александров Максим (капитан)
Мальцева Ангелина
Пшеницын Максим
Сорокин Александр

# Описание задачи
Есть уже написанный тестовый проект , работающий с RabbitMQ и Postgre$QL. При
запуске проекта возникает ошибка: P$QLException: ERROR: duplicate key value violates
unique constraint.

# Задание
Исправить возникшую ошибку, не нарушая работу программы. Важно, чтобы ошибка
была устранена на уровне AmqpListener, а не в SenderService

# Ошибка
Ошибка возникала при параллельном выполнении нескольких похожих запросов

# Исправение 
Одним из решений ошибки является синхронизация потоков, чтобы запросы не параллелились. Для этого нужно добавить ключевое слово synchronized в файле DomainServiceLmpl.java следующим образом
```
public synchronized void saveHakatonEntity(SendingDto sendingDto) {
    if(!hakatonEntityRepository.existsHakatonEntityByName(sendingDto.getName())){
        hakatonEntityRepository.save(new HakatonEntity(sendingDto.getName()));
    }
}
```
