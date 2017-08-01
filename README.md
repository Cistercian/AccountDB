# AccountDB
---

Приложение демонстрирует работы с БД. Предоставляет возможность осуществлять CRUD операции с записями. Реализует многопоточность при заполнении БД большим количеством записей (2 000 000).
Приложение написано с использованием фреймворка Spring(используются Spring, JSF, JPA с провайдером Hibernate). Используемая СУБД: MySQL.

### Структура БД:

Служебные функции по пакетной очистке БД и создания новых записец расположены на странице "Редактирование БД". В связи с работой с большим количеством записей, инициировать процедуры нежелательно - например, время создания 2 000 000 записей составляет порядка 1,5 часа.
