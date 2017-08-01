# AccountDB
---

Приложение демонстрирует работу с БД, предоставляя возможность осуществлять CRUD операции с записями. Реализует многопоточность при заполнении БД большим количеством записей (2 000 000).
Приложение написано на java с использованием фреймворка Spring(используются Spring MVC, JSP, JPA с провайдером Hibernate). Используемая СУБД: MySQL.

**Развернутое приложение:** <http://5.189.96.147:83>

---

### Структура БД:
![Схема](http://5.189.96.147/schema_account_db.png)

CREATE TABLE `accounts` (

  `ID` int(11) NOT NULL AUTO_INCREMENT,
  
  `acct` int(7) DEFAULT NULL,
  
  PRIMARY KEY (`ID`),
  
  UNIQUE KEY `acct` (`acct`)
  
) ENGINE=InnoDB AUTO_INCREMENT=2102006 DEFAULT CHARSET=utf8;

---
CREATE TABLE `clients` (

  `ID` int(11) NOT NULL AUTO_INCREMENT,
  
  `name` char(60) DEFAULT NULL,
  
  `account` int(11) DEFAULT NULL,
  
  PRIMARY KEY (`ID`),
  
  KEY `FKl609765alqsk2x2s7lclwelr9` (`account`),
  
  KEY `name` (`name`),
  
  CONSTRAINT `FKl609765alqsk2x2s7lclwelr9` FOREIGN KEY (`account`) REFERENCES `accounts` (`ID`)
  
) ENGINE=InnoDB AUTO_INCREMENT=2102204 DEFAULT CHARSET=utf8;

---

Схема взаимодействия на програмном уровне:
### DAO уровень
Представлен 2 классами: `Account`, `Client`, связанные связью OneToOne (поле account таблицы clients).

### Уровень контроллеров
Представлен 1 классом: `AccountController`.

### Сервисный уровень
Опять же 1 класс `AccountServiceImpl`, осуществляющий все манипуляции с данными БД. Служебная функция заполнения БД использует внутренний класс `CreatorEntities` для создания 10 потоков, осуществляющих паралельное сохранение пар записей таблиц `accounts`-`clients` в БД (1 000 000 пар записей создается за ~1 100 сек). Ручной поиск данных осуществляется по полному совпадению с проиндексируемымы полями `accounts.acct` или `clients.name`. Ответ с результатом операций поиска, создания или удаления записей возвращается в виде объекта класса `JsonResponse`, содержащего в себе тип ответа (enum `JsonResponseType`), текст (строковое описание ответа), объект `EntityDatа` (несущий информацию из полей `accounts.id`, `clients.name` и `accounts.acct`).

### Описание функционала фронт-енда

Служебные функции по пакетной очистке БД и создания новых записей расположены на странице "Редактирование БД". В связи с работой с большим количеством записей, инициировать процедуры нежелательно - например, время создания 2 000 000 записей составляет порядка 1,5 часа.

CRUD операции с записями осуществляются на главной странице. 
* При нажатии на кнопку "Найти" происходит запрос искомой строки в всплывающем окне. Результат поиска отображается в нижней части страницы (Отображается первый найденный результат. При нахождении более одной записи появляется соответствующее предупреждение в всплывающем окне). 
* При нажатии на кнопку Удалить при заполненных ранее найденных полях происходит запрос подтверждения на удаление и, при утвердительном ответе, найденная пара значений таблиц accounts-clients удаляется. 
* При нажатии на кнопку "Вставить" происходит попытка сохранить пару записей таблиц с переданными полями "ФИО абонента" (clients.name) и "Лицевой счет абонента" (accounts.acct). На клиентской стороне намеряно не осуществляется валидация передаваемых значений. При нарушении консистентности данных, проверяемой на стороне СУБД, возникшая ошибка транслируется на фронт  и отображается в всплывающем окне.
