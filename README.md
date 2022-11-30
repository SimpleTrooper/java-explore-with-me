[Пул-реквест](https://github.com/SimpleTrooper/java-explore-with-me/pull/1)

# Explore with me.
REST-API для приложения организации ивентов.

# Фича - добавление локаций администратором и поиск событий в локации.

Локация - круг на карте с координатами (широта, долгота) и радиусом в километрах. Каждое событие содержит координаты. 
Считается, что событие происходит в определенной локации, если координаты этого события лежат внутри заданного круга.
Локация имеет тип - COUNTRY (Страна), STATE (Регион), CITY (Город), ADDRESS (Адрес) и PLACE (Неопределенное место).
Локации добавляются администратором и при добавлении происходит запрос на удаленное api https://graphhopper.com/api/1
для получения сведений о месте на карте по его координатам (обратное геокодирование).

Добавлены эндпоинты основному сервису:

__GET /admin/locations?locationIds={Список id локаций}&from={от}&size={размер}__ - получение всех локаций
Ответ - список AdminLocationDto.

__POST /admin/locations__ - добавление новой локации. Тело - NewLocationDto,
ответ - AdminLocationDto

__PATCH /admin/locations__ - обновление информации о локации. Тело - NewLocationDto,
ответ - AdminLocationDto

__DELETE /admin/locations/{id}__ - удаление локации по id

__PATCH /admin/locations/{id}/resolve__ - обновить информацию о локации с сервиса геокодирования
ответ - AdminLocationDto

AdminLocationDto -

    {
        "id": id,
        "name": "Название",
        "description": "Описание",
        "lat": Широта,  
        "lon": Долгота,
        "radius": Радиус в км,
        "country": "Страна",
        "state": "Регион",
        "city": "Город",
        "postalCode": "Почтовый индекс",
        "street": "Улица",
        "housenumber": "Номер дома",
        "type": "Тип локации",
        "resolved": Получена ли информация с сервиса геокодирования - boolean,
        "resolveDate": Дата последнего обновления информации с сервиса геокодирования
    }

NewLocationDto -

    {
        "id": id,
        "name": "Название",
        "description": "Описание",
        "lat": Широта,  
        "lon": Долгота,
        "radius": Радиус в км,
        "type": "Тип локации"
    }

__GET /locations?from={от}&size={размер}__ - получить все локации. Ответ - список PublicLocationDto

__GET /locations/{locationId}__ - получить локацию по id.
Ответ - PublicLocationDto

__GET /events?location={locationId}__ - поиск всех событий в указанной локации. 
Ответ - список PublicEventShortDto

PublicLocationDto -

    {
        "id": id,
        "name": "Название",
        "description": "Описание",
        "lat": Широта,  
        "lon": Долгота,
        "radius": Радиус в км,
        "country": "Страна",
        "state": "Регион",
        "city": "Город",
        "postalCode": "Почтовый индекс",
        "street": "Улица",
        "housenumber": "Номер дома",
        "type": "Тип локации",
        "resolved": Получена ли информация с сервиса геокодирования - boolean,
        "resolveDate": Дата последнего обновления информации с сервиса геокодирования
    }

PublicEventShortDto -

    {
        "annotation": "Аннотация",
        "category": {
                        "id": id,
                        "name": "Название категории"  
                    },
        "confirmedRequests": "Количество подтвержденных участников",
        "eventDate": "Дата события",  
        "id": id,
        "initiator": {
                         "id": id,
                         "name": "Имя пользователя"  
                     },
        "paid": Платное либо бесплатное,
        "title": "Название",
        "views": "Количество просмотров"
    }

Добавлен сервис геокодирования. Его эндпоинты: 

__GET /geocode?lat={широта}&lon={долгота}&type={тип локации} - получить информацию о локации по координатам. 
Ответ - OutputLocationDto

OutputLocationDto -

    {
        "country": "Страна",
        "state": "Регион",
        "city": "Город",
        "postalCode": "Почтовый индекс",
        "street": "Улица",
        "housenumber": "Номер дома",
        "type": "Тип локации",
        "resolved": Получена ли информация с сервиса геокодирования - boolean,
        "resolveDate": Дата последнего обновления информации с сервиса геокодирования
    }

![ER-Диаграмма основной БД](/png/ExploreER.png)
![ER-Диаграмма БД статистики](/png/StatisticsER.png)
