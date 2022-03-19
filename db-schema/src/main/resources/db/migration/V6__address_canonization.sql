CREATE TABLE region (
	id serial,
	"name" varchar(100) NOT NULL,

	CONSTRAINT pk_region PRIMARY KEY (id),
	CONSTRAINT un_region_name UNIQUE (name)
);

CREATE TABLE city (
	id serial,
	region_id integer NULL,
	"name" varchar(100) NOT NULL,

	CONSTRAINT pk_city PRIMARY KEY (id),
	CONSTRAINT un_city_region_name UNIQUE (region_id, name),
	CONSTRAINT fk_city_region FOREIGN KEY (region_id) REFERENCES region(id) ON DELETE CASCADE ON UPDATE RESTRICT
);

ALTER TABLE address
ADD COLUMN city_id integer NOT NULL,
DROP COLUMN region,
DROP COLUMN city,
ADD CONSTRAINT fk_address_city FOREIGN KEY (city_id) REFERENCES city(id) ON DELETE RESTRICT ON UPDATE RESTRICT;

insert into region(name) values
('Вінницька'),
('Волинська'),
('Дніпропетровська'),
('Донецька'),
('Житомирська'),
('Закарпатська'),
('Запорізька'),
('Івано-Франківська'),
('Київська'),
('Кіровоградська'),
('Луганська'),
('Львівська'),
('Миколаївська'),
('Одеська'),
('Полтавська'),
('Рівненська'),
('Сумська'),
('Тернопільська'),
('Харківська'),
('Херсонська'),
('Хмельницька'),
('Черкаська'),
('Чернігівська'),
('Чернівецька');

-- Вінницька
insert into city(region_id, name)
select t.*
from (
  select id, 'Іллінці' from region where name = 'Вінницька'
  union
  select id, 'Бабин' from region where name = 'Вінницька'
  union
  select id, 'Бар' from region where name = 'Вінницька'
  union
  select id, 'Бершадь' from region where name = 'Вінницька'
  union
  select id, 'Брацлав' from region where name = 'Вінницька'
  union
  select id, 'Браїлів' from region where name = 'Вінницька'
  union
  select id, 'Вапнярка' from region where name = 'Вінницька'
  union
  select id, 'Великий Митник' from region where name = 'Вінницька'
  union
  select id, 'Вендичани' from region where name = 'Вінницька'
  union
  select id, 'Вороновиця' from region where name = 'Вінницька'
  union
  select id, 'Вінницькі Хутора' from region where name = 'Вінницька'
  union
  select id, 'Вінниця' from region where name = 'Вінницька'
  union
  select id, 'Гайсин' from region where name = 'Вінницька'
  union
  select id, 'Глухівці' from region where name = 'Вінницька'
  union
  select id, 'Гнівань' from region where name = 'Вінницька'
  union
  select id, 'Городківка' from region where name = 'Вінницька'
  union
  select id, 'Дашів' from region where name = 'Вінницька'
  union
  select id, 'Джурин' from region where name = 'Вінницька'
  union
  select id, 'Дзигівка' from region where name = 'Вінницька'
  union
  select id, 'Жмеринка' from region where name = 'Вінницька'
  union
  select id, 'Зарванці' from region where name = 'Вінницька'
  union
  select id, 'Калинівка' from region where name = 'Вінницька'
  union
  select id, 'Клембівка' from region where name = 'Вінницька'
  union
  select id, 'Козятин' from region where name = 'Вінницька'
  union
  select id, 'Козятин' from region where name = 'Вінницька'
  union
  select id, 'Копайгород' from region where name = 'Вінницька'
  union
  select id, 'Корделівка' from region where name = 'Вінницька'
  union
  select id, 'Крижопіль' from region where name = 'Вінницька'
  union
  select id, 'Ладижин' from region where name = 'Вінницька'
  union
  select id, 'Липовець' from region where name = 'Вінницька'
  union
  select id, 'Літин' from region where name = 'Вінницька'
  union
  select id, 'Могилів-Подільський' from region where name = 'Вінницька'
  union
  select id, 'Муровані Курилівці' from region where name = 'Вінницька'
  union
  select id, 'Немирів' from region where name = 'Вінницька'
  union
  select id, 'Ободівка' from region where name = 'Вінницька'
  union
  select id, 'Оратів' from region where name = 'Вінницька'
  union
  select id, 'Погребище' from region where name = 'Вінницька'
  union
  select id, 'Піщанка' from region where name = 'Вінницька'
  union
  select id, 'Росоша' from region where name = 'Вінницька'
  union
  select id, 'Ситківці' from region where name = 'Вінницька'
  union
  select id, 'Соболівка' from region where name = 'Вінницька'
  union
  select id, 'Стрижавка' from region where name = 'Вінницька'
  union
  select id, 'Сутиски' from region where name = 'Вінницька'
  union
  select id, 'Теплик' from region where name = 'Вінницька'
  union
  select id, 'Тиврів' from region where name = 'Вінницька'
  union
  select id, 'Тиврів' from region where name = 'Вінницька'
  union
  select id, 'Томашпіль' from region where name = 'Вінницька'
  union
  select id, 'Тростянець' from region where name = 'Вінницька'
  union
  select id, 'Тульчин' from region where name = 'Вінницька'
  union
  select id, 'Турбів' from region where name = 'Вінницька'
  union
  select id, 'Уланов' from region where name = 'Вінницька'
  union
  select id, 'Хмільник' from region where name = 'Вінницька'
  union
  select id, 'Чернівці' from region where name = 'Вінницька'
  union
  select id, 'Чечельник' from region where name = 'Вінницька'
  union
  select id, 'Шаргород' from region where name = 'Вінницька'
  union
  select id, 'Шпиків' from region where name = 'Вінницька'
  union
  select id, 'Ялтушків' from region where name = 'Вінницька'
  union
  select id, 'Ямпіль' from region where name = 'Вінницька'
) t;

-- Волинська
insert into city(region_id, name)
select t.*
from (
  select id, 'Іваничі' from region where name = 'Волинська'
  union
  select id, 'Берестечко' from region where name = 'Волинська'
  union
  select id, 'Боратин' from region where name = 'Волинська'
  union
  select id, 'Великий Омеляник' from region where name = 'Волинська'
  union
  select id, 'Володимир-Волинський' from region where name = 'Волинська'
  union
  select id, 'Голоби' from region where name = 'Волинська'
  union
  select id, 'Городок' from region where name = 'Волинська'
  union
  select id, 'Горохів' from region where name = 'Волинська'
  union
  select id, 'Камінь-Каширський' from region where name = 'Волинська'
  union
  select id, 'Ковель' from region where name = 'Волинська'
  union
  select id, 'Колки' from region where name = 'Волинська'
  union
  select id, 'Ківерці' from region where name = 'Волинська'
  union
  select id, 'Липини' from region where name = 'Волинська'
  union
  select id, 'Локачі' from region where name = 'Волинська'
  union
  select id, 'Луків' from region where name = 'Волинська'
  union
  select id, 'Луцьк' from region where name = 'Волинська'
  union
  select id, 'Любешів' from region where name = 'Волинська'
  union
  select id, 'Люблинець' from region where name = 'Волинська'
  union
  select id, 'Любомль' from region where name = 'Волинська'
  union
  select id, 'Маневичі' from region where name = 'Волинська'
  union
  select id, 'Нововолинськ' from region where name = 'Волинська'
  union
  select id, 'Олика' from region where name = 'Волинська'
  union
  select id, 'Підгайці' from region where name = 'Волинська'
  union
  select id, 'Ратне' from region where name = 'Волинська'
  union
  select id, 'Рожище' from region where name = 'Волинська'
  union
  select id, 'Стара Вижівка' from region where name = 'Волинська'
  union
  select id, 'Старовойтове' from region where name = 'Волинська'
  union
  select id, 'Струмівка' from region where name = 'Волинська'
  union
  select id, 'Торчин' from region where name = 'Волинська'
  union
  select id, 'Турійськ' from region where name = 'Волинська'
  union
  select id, 'Устилуг' from region where name = 'Волинська'
  union
  select id, 'Цумань' from region where name = 'Волинська'
  union
  select id, 'Шацьк' from region where name = 'Волинська'
) t;

-- Дніпропетровська
insert into city(region_id, name)
select t.*
from (
  select id, 'Іларіонове' from region where name = 'Дніпропетровська'
  union
  select id, 'Апостолово' from region where name = 'Дніпропетровська'
  union
  select id, 'Аули' from region where name = 'Дніпропетровська'
  union
  select id, 'Васильківка' from region where name = 'Дніпропетровська'
  union
  select id, 'Верхньодніпровськ' from region where name = 'Дніпропетровська'
  union
  select id, 'Верхівцеве' from region where name = 'Дніпропетровська'
  union
  select id, 'Вишневе' from region where name = 'Дніпропетровська'
  union
  select id, 'Вільне' from region where name = 'Дніпропетровська'
  union
  select id, 'Вільногірськ' from region where name = 'Дніпропетровська'
  union
  select id, 'Гвардійське' from region where name = 'Дніпропетровська'
  union
  select id, 'Голубівка' from region where name = 'Дніпропетровська'
  union
  select id, 'Губиниха' from region where name = 'Дніпропетровська'
  union
  select id, 'Дніпро' from region where name = 'Дніпропетровська'
  union
  select id, 'Дніпровське' from region where name = 'Дніпропетровська'
  union
  select id, 'Дослідне' from region where name = 'Дніпропетровська'
  union
  select id, 'Жовті Води' from region where name = 'Дніпропетровська'
  union
  select id, 'Зеленодольськ' from region where name = 'Дніпропетровська'
  union
  select id, 'Кам''янка' from region where name = 'Дніпропетровська'
  union
  select id, 'Кам''янське' from region where name = 'Дніпропетровська'
  union
  select id, 'Кривий Ріг' from region where name = 'Дніпропетровська'
  union
  select id, 'Кринички' from region where name = 'Дніпропетровська'
  union
  select id, 'Кіровське' from region where name = 'Дніпропетровська'
  union
  select id, 'Лозуватка' from region where name = 'Дніпропетровська'
  union
  select id, 'Магдалинівка' from region where name = 'Дніпропетровська'
  union
  select id, 'Марганець' from region where name = 'Дніпропетровська'
  union
  select id, 'Межова' from region where name = 'Дніпропетровська'
  union
  select id, 'Меліоративне' from region where name = 'Дніпропетровська'
  union
  select id, 'Нива Трудова' from region where name = 'Дніпропетровська'
  union
  select id, 'Новомосковськ' from region where name = 'Дніпропетровська'
  union
  select id, 'Новоолександрівка' from region where name = 'Дніпропетровська'
  union
  select id, 'Нікополь' from region where name = 'Дніпропетровська'
  union
  select id, 'Олексіївка' from region where name = 'Дніпропетровська'
  union
  select id, 'П''ятихатки' from region where name = 'Дніпропетровська'
  union
  select id, 'Павлоград' from region where name = 'Дніпропетровська'
  union
  select id, 'Перещепине' from region where name = 'Дніпропетровська'
  union
  select id, 'Першотравенськ' from region where name = 'Дніпропетровська'
  union
  select id, 'Петриківка' from region where name = 'Дніпропетровська'
  union
  select id, 'Петропавлівка' from region where name = 'Дніпропетровська'
  union
  select id, 'Покров' from region where name = 'Дніпропетровська'
  union
  select id, 'Покровське' from region where name = 'Дніпропетровська'
  union
  select id, 'Просяна' from region where name = 'Дніпропетровська'
  union
  select id, 'Підгородне' from region where name = 'Дніпропетровська'
  union
  select id, 'Піщанка' from region where name = 'Дніпропетровська'
  union
  select id, 'Радушне' from region where name = 'Дніпропетровська'
  union
  select id, 'Синельникове' from region where name = 'Дніпропетровська'
  union
  select id, 'Солоне' from region where name = 'Дніпропетровська'
  union
  select id, 'Софіївка' from region where name = 'Дніпропетровська'
  union
  select id, 'Тернівка' from region where name = 'Дніпропетровська'
  union
  select id, 'Томаківка' from region where name = 'Дніпропетровська'
  union
  select id, 'Христофорівка' from region where name = 'Дніпропетровська'
  union
  select id, 'Царичанка' from region where name = 'Дніпропетровська'
  union
  select id, 'Чаплине' from region where name = 'Дніпропетровська'
  union
  select id, 'Червоногригорівка' from region where name = 'Дніпропетровська'
  union
  select id, 'Широке' from region where name = 'Дніпропетровська'
  union
  select id, 'Шолохове' from region where name = 'Дніпропетровська'
  union
  select id, 'Ювілейне' from region where name = 'Дніпропетровська'
  union
  select id, 'Юр''ївка' from region where name = 'Дніпропетровська'
) t;

-- Донецька
insert into city(region_id, name)
select t.*
from (
  select id, 'Іловайськ' from region where name = 'Донецька'
  union
  select id, 'Авдіївка' from region where name = 'Донецька'
  union
  select id, 'Амвросіївка' from region where name = 'Донецька'
  union
  select id, 'Бахмут' from region where name = 'Донецька'
  union
  select id, 'Білицьке' from region where name = 'Донецька'
  union
  select id, 'Білозерське' from region where name = 'Донецька'
  union
  select id, 'Велика Новосілка' from region where name = 'Донецька'
  union
  select id, 'Волноваха' from region where name = 'Донецька'
  union
  select id, 'Володарське' from region where name = 'Донецька'
  union
  select id, 'Володимирівка' from region where name = 'Донецька'
  union
  select id, 'Вугледар' from region where name = 'Донецька'
  union
  select id, 'Гірник' from region where name = 'Донецька'
  union
  select id, 'Добропілля' from region where name = 'Донецька'
  union
  select id, 'Долина' from region where name = 'Донецька'
  union
  select id, 'Донецьк' from region where name = 'Донецька'
  union
  select id, 'Донське' from region where name = 'Донецька'
  union
  select id, 'Дружківка' from region where name = 'Донецька'
  union
  select id, 'Костянтинівка' from region where name = 'Донецька'
  union
  select id, 'Краматорськ' from region where name = 'Донецька'
  union
  select id, 'Курахово' from region where name = 'Донецька'
  union
  select id, 'Лиман' from region where name = 'Донецька'
  union
  select id, 'Макіївка' from region where name = 'Донецька'
  union
  select id, 'Мангуш' from region where name = 'Донецька'
  union
  select id, 'Мар''їнка' from region where name = 'Донецька'
  union
  select id, 'Маріуполь' from region where name = 'Донецька'
  union
  select id, 'Миколаївка' from region where name = 'Донецька'
  union
  select id, 'Мирноград' from region where name = 'Донецька'
  union
  select id, 'Новгородське' from region where name = 'Донецька'
  union
  select id, 'Новий Світ' from region where name = 'Донецька'
  union
  select id, 'Новоазовськ' from region where name = 'Донецька'
  union
  select id, 'Новогродівка' from region where name = 'Донецька'
  union
  select id, 'Новодонецьке' from region where name = 'Донецька'
  union
  select id, 'Новоекономічне' from region where name = 'Донецька'
  union
  select id, 'Новотроїцьке' from region where name = 'Донецька'
  union
  select id, 'Олександрівка' from region where name = 'Донецька'
  union
  select id, 'Очеретине' from region where name = 'Донецька'
  union
  select id, 'Покровськ' from region where name = 'Донецька'
  union
  select id, 'Райгородок' from region where name = 'Донецька'
  union
  select id, 'Родинське' from region where name = 'Донецька'
  union
  select id, 'Сартана' from region where name = 'Донецька'
  union
  select id, 'Святогірськ' from region where name = 'Донецька'
  union
  select id, 'Світлодарськ' from region where name = 'Донецька'
  union
  select id, 'Селидове' from region where name = 'Донецька'
  union
  select id, 'Слов''янськ' from region where name = 'Донецька'
  union
  select id, 'Соледар' from region where name = 'Донецька'
  union
  select id, 'Старобешеве' from region where name = 'Донецька'
  union
  select id, 'Сіверськ' from region where name = 'Донецька'
  union
  select id, 'Торецьк' from region where name = 'Донецька'
  union
  select id, 'Українськ' from region where name = 'Донецька'
  union
  select id, 'Цукурине' from region where name = 'Донецька'
  union
  select id, 'Часів Яр' from region where name = 'Донецька'
  union
  select id, 'Черкаське' from region where name = 'Донецька'
  union
  select id, 'Шахтарськ' from region where name = 'Донецька'
  union
  select id, 'Ясинувата' from region where name = 'Донецька'
) t;

-- Житомирська
insert into city(region_id, name)
select t.*
from (
  select id, 'Ємільчине' from region where name = 'Житомирська'
  union
  select id, 'Іванопіль' from region where name = 'Житомирська'
  union
  select id, 'Іванівка' from region where name = 'Житомирська'
  union
  select id, 'Іршанськ' from region where name = 'Житомирська'
  union
  select id, 'Андрушівка' from region where name = 'Житомирська'
  union
  select id, 'Баранівка' from region where name = 'Житомирська'
  union
  select id, 'Бердичів' from region where name = 'Житомирська'
  union
  select id, 'Брусилів' from region where name = 'Житомирська'
  union
  select id, 'Довжик' from region where name = 'Житомирська'
  union
  select id, 'Житомир' from region where name = 'Житомирська'
  union
  select id, 'Корнин' from region where name = 'Житомирська'
  union
  select id, 'Коростень' from region where name = 'Житомирська'
  union
  select id, 'Коростишів' from region where name = 'Житомирська'
  union
  select id, 'Лугини' from region where name = 'Житомирська'
  union
  select id, 'Любар' from region where name = 'Житомирська'
  union
  select id, 'Малин' from region where name = 'Житомирська'
  union
  select id, 'Мирне' from region where name = 'Житомирська'
  union
  select id, 'Миропіль' from region where name = 'Житомирська'
  union
  select id, 'Народичі' from region where name = 'Житомирська'
  union
  select id, 'Нова Борова' from region where name = 'Житомирська'
  union
  select id, 'Новоград-Волинський' from region where name = 'Житомирська'
  union
  select id, 'Новогуйвинське' from region where name = 'Житомирська'
  union
  select id, 'Нові Білокоровичі' from region where name = 'Житомирська'
  union
  select id, 'Овруч' from region where name = 'Житомирська'
  union
  select id, 'Озерне' from region where name = 'Житомирська'
  union
  select id, 'Олевськ' from region where name = 'Житомирська'
  union
  select id, 'Першотравенськ' from region where name = 'Житомирська'
  union
  select id, 'Першотравневе' from region where name = 'Житомирська'
  union
  select id, 'Попільня' from region where name = 'Житомирська'
  union
  select id, 'Радомишль' from region where name = 'Житомирська'
  union
  select id, 'Романов' from region where name = 'Житомирська'
  union
  select id, 'Ружин' from region where name = 'Житомирська'
  union
  select id, 'Червоне' from region where name = 'Житомирська'
  union
  select id, 'Червоноармійськ' from region where name = 'Житомирська'
  union
  select id, 'Черняхів' from region where name = 'Житомирська'
  union
  select id, 'Чоповичі' from region where name = 'Житомирська'
  union
  select id, 'Чуднов' from region where name = 'Житомирська'
) t;

-- Закарпатська
insert into city(region_id, name)
select t.*
from (
  select id, 'Іза' from region where name = 'Закарпатська'
  union
  select id, 'Ільниця' from region where name = 'Закарпатська'
  union
  select id, 'Іршава' from region where name = 'Закарпатська'
  union
  select id, 'Баранинці' from region where name = 'Закарпатська'
  union
  select id, 'Бедевля' from region where name = 'Закарпатська'
  union
  select id, 'Берегове' from region where name = 'Закарпатська'
  union
  select id, 'Богдан' from region where name = 'Закарпатська'
  union
  select id, 'Боронява' from region where name = 'Закарпатська'
  union
  select id, 'Буштино' from region where name = 'Закарпатська'
  union
  select id, 'Білки' from region where name = 'Закарпатська'
  union
  select id, 'Великий Березний' from region where name = 'Закарпатська'
  union
  select id, 'Великий Бичків' from region where name = 'Закарпатська'
  union
  select id, 'Великі Лази' from region where name = 'Закарпатська'
  union
  select id, 'Великі Лучки' from region where name = 'Закарпатська'
  union
  select id, 'Верхнє Водяне' from region where name = 'Закарпатська'
  union
  select id, 'Вилок' from region where name = 'Закарпатська'
  union
  select id, 'Виноградів' from region where name = 'Закарпатська'
  union
  select id, 'Вишково' from region where name = 'Закарпатська'
  union
  select id, 'Воловець' from region where name = 'Закарпатська'
  union
  select id, 'Ганичі' from region where name = 'Закарпатська'
  union
  select id, 'Грушево' from region where name = 'Закарпатська'
  union
  select id, 'Добрянське' from region where name = 'Закарпатська'
  union
  select id, 'Драгово' from region where name = 'Закарпатська'
  union
  select id, 'Дубове' from region where name = 'Закарпатська'
  union
  select id, 'Калини' from region where name = 'Закарпатська'
  union
  select id, 'Королево' from region where name = 'Закарпатська'
  union
  select id, 'Малий Березний' from region where name = 'Закарпатська'
  union
  select id, 'Минай' from region where name = 'Закарпатська'
  union
  select id, 'Мукачево' from region where name = 'Закарпатська'
  union
  select id, 'Міжгір''я' from region where name = 'Закарпатська'
  union
  select id, 'Нересниця' from region where name = 'Закарпатська'
  union
  select id, 'Нижні Ворота' from region where name = 'Закарпатська'
  union
  select id, 'Перечин' from region where name = 'Закарпатська'
  union
  select id, 'Поляна' from region where name = 'Закарпатська'
  union
  select id, 'Рахів' from region where name = 'Закарпатська'
  union
  select id, 'Рокосово' from region where name = 'Закарпатська'
  union
  select id, 'Свалява' from region where name = 'Закарпатська'
  union
  select id, 'Середнє' from region where name = 'Закарпатська'
  union
  select id, 'Сокирниця' from region where name = 'Закарпатська'
  union
  select id, 'Соломоново' from region where name = 'Закарпатська'
  union
  select id, 'Солотвино' from region where name = 'Закарпатська'
  union
  select id, 'Тересва' from region where name = 'Закарпатська'
  union
  select id, 'Терново' from region where name = 'Закарпатська'
  union
  select id, 'Тячів' from region where name = 'Закарпатська'
  union
  select id, 'Угля' from region where name = 'Закарпатська'
  union
  select id, 'Ужгород' from region where name = 'Закарпатська'
  union
  select id, 'Хуст' from region where name = 'Закарпатська'
  union
  select id, 'Чоп' from region where name = 'Закарпатська'
  union
  select id, 'Ясиня' from region where name = 'Закарпатська'
) t;

-- Запорізька
insert into city(region_id, name)
select t.*
from (
  select id, 'Андріївка' from region where name = 'Запорізька'
  union
  select id, 'Балабине' from region where name = 'Запорізька'
  union
  select id, 'Бердянськ' from region where name = 'Запорізька'
  union
  select id, 'Більмак' from region where name = 'Запорізька'
  union
  select id, 'Василівка' from region where name = 'Запорізька'
  union
  select id, 'Велика Білозерка' from region where name = 'Запорізька'
  union
  select id, 'Веселе' from region where name = 'Запорізька'
  union
  select id, 'Вільнянськ' from region where name = 'Запорізька'
  union
  select id, 'Гуляйполе' from region where name = 'Запорізька'
  union
  select id, 'Дніпрорудне' from region where name = 'Запорізька'
  union
  select id, 'Енергодар' from region where name = 'Запорізька'
  union
  select id, 'Запоріжжя' from region where name = 'Запорізька'
  union
  select id, 'Кам''янка-Дніпровська' from region where name = 'Запорізька'
  union
  select id, 'Кирилівка' from region where name = 'Запорізька'
  union
  select id, 'Комишуваха' from region where name = 'Запорізька'
  union
  select id, 'Костянтинівка' from region where name = 'Запорізька'
  union
  select id, 'Кушугум' from region where name = 'Запорізька'
  union
  select id, 'Малокатеринівка' from region where name = 'Запорізька'
  union
  select id, 'Мелітополь' from region where name = 'Запорізька'
  union
  select id, 'Михайлівка' from region where name = 'Запорізька'
  union
  select id, 'Молочанськ' from region where name = 'Запорізька'
  union
  select id, 'Новобогданівка' from region where name = 'Запорізька'
  union
  select id, 'Новомиколаївка' from region where name = 'Запорізька'
  union
  select id, 'Новопетрівка' from region where name = 'Запорізька'
  union
  select id, 'Орєхов' from region where name = 'Запорізька'
  union
  select id, 'Осипенко' from region where name = 'Запорізька'
  union
  select id, 'Пологи' from region where name = 'Запорізька'
  union
  select id, 'Приазовське' from region where name = 'Запорізька'
  union
  select id, 'Приморськ' from region where name = 'Запорізька'
  union
  select id, 'Пришиб' from region where name = 'Запорізька'
  union
  select id, 'Розівка' from region where name = 'Запорізька'
  union
  select id, 'Семенівка' from region where name = 'Запорізька'
  union
  select id, 'Тернувате' from region where name = 'Запорізька'
  union
  select id, 'Токмак' from region where name = 'Запорізька'
  union
  select id, 'Чернігівка' from region where name = 'Запорізька'
  union
  select id, 'Широке' from region where name = 'Запорізька'
  union
  select id, 'Якимівка' from region where name = 'Запорізька'
) t;

-- Івано-Франківська
insert into city(region_id, name)
select t.*
from (
  select id, 'Єзупіль' from region where name = 'Івано-Франківська'
  union
  select id, 'Івано-Франківськ' from region where name = 'Івано-Франківська'
  union
  select id, 'Богородчани' from region where name = 'Івано-Франківська'
  union
  select id, 'Болехів' from region where name = 'Івано-Франківська'
  union
  select id, 'Брошнів-Осада' from region where name = 'Івано-Франківська'
  union
  select id, 'Бурштин' from region where name = 'Івано-Франківська'
  union
  select id, 'Верховина' from region where name = 'Івано-Франківська'
  union
  select id, 'Вигода' from region where name = 'Івано-Франківська'
  union
  select id, 'Ворохта' from region where name = 'Івано-Франківська'
  union
  select id, 'Галич' from region where name = 'Івано-Франківська'
  union
  select id, 'Гвіздець' from region where name = 'Івано-Франківська'
  union
  select id, 'Городенка' from region where name = 'Івано-Франківська'
  union
  select id, 'Делятин' from region where name = 'Івано-Франківська'
  union
  select id, 'Джурів' from region where name = 'Івано-Франківська'
  union
  select id, 'Долина' from region where name = 'Івано-Франківська'
  union
  select id, 'Заболотів' from region where name = 'Івано-Франківська'
  union
  select id, 'Загвіздя' from region where name = 'Івано-Франківська'
  union
  select id, 'Калуш' from region where name = 'Івано-Франківська'
  union
  select id, 'Коломия' from region where name = 'Івано-Франківська'
  union
  select id, 'Косів' from region where name = 'Івано-Франківська'
  union
  select id, 'Кути' from region where name = 'Івано-Франківська'
  union
  select id, 'Ланчин' from region where name = 'Івано-Франківська'
  union
  select id, 'Лисець' from region where name = 'Івано-Франківська'
  union
  select id, 'Микуличин' from region where name = 'Івано-Франківська'
  union
  select id, 'Надвірна' from region where name = 'Івано-Франківська'
  union
  select id, 'Новиця' from region where name = 'Івано-Франківська'
  union
  select id, 'Новоселиця' from region where name = 'Івано-Франківська'
  union
  select id, 'Обертин' from region where name = 'Івано-Франківська'
  union
  select id, 'Отинія' from region where name = 'Івано-Франківська'
  union
  select id, 'Поляниця' from region where name = 'Івано-Франківська'
  union
  select id, 'Підмихайля' from region where name = 'Івано-Франківська'
  union
  select id, 'Рогатин' from region where name = 'Івано-Франківська'
  union
  select id, 'Рожнятів' from region where name = 'Івано-Франківська'
  union
  select id, 'Снятин' from region where name = 'Івано-Франківська'
  union
  select id, 'Снятин' from region where name = 'Івано-Франківська'
  union
  select id, 'Солотвин' from region where name = 'Івано-Франківська'
  union
  select id, 'Тисмениця' from region where name = 'Івано-Франківська'
  union
  select id, 'Тисмениця' from region where name = 'Івано-Франківська'
  union
  select id, 'Тлумач' from region where name = 'Івано-Франківська'
  union
  select id, 'Чернятин' from region where name = 'Івано-Франківська'
  union
  select id, 'Яблунів' from region where name = 'Івано-Франківська'
  union
  select id, 'Ямниця' from region where name = 'Івано-Франківська'
  union
  select id, 'Яремче' from region where name = 'Івано-Франківська'
) t;

-- Київська
insert into city(region_id, name)
select t.*
from (
  select id, 'Іванків' from region where name = 'Київська'
  union
  select id, 'Ірпінь' from region where name = 'Київська'
  union
  select id, 'Бабинці' from region where name = 'Київська'
  union
  select id, 'Баришівка' from region where name = 'Київська'
  union
  select id, 'Березань' from region where name = 'Київська'
  union
  select id, 'Богданівка' from region where name = 'Київська'
  union
  select id, 'Богуслав' from region where name = 'Київська'
  union
  select id, 'Бориспіль' from region where name = 'Київська'
  union
  select id, 'Борова' from region where name = 'Київська'
  union
  select id, 'Бородянка' from region where name = 'Київська'
  union
  select id, 'Боярка' from region where name = 'Київська'
  union
  select id, 'Бровари' from region where name = 'Київська'
  union
  select id, 'Буча' from region where name = 'Київська'
  union
  select id, 'Біла Церква' from region where name = 'Київська'
  union
  select id, 'Білогородка' from region where name = 'Київська'
  union
  select id, 'Васильків' from region where name = 'Київська'
  union
  select id, 'Вишгород' from region where name = 'Київська'
  union
  select id, 'Вишневе' from region where name = 'Київська'
  union
  select id, 'Володарка' from region where name = 'Київська'
  union
  select id, 'Ворзель' from region where name = 'Київська'
  union
  select id, 'Віта-Поштова' from region where name = 'Київська'
  union
  select id, 'Глеваха' from region where name = 'Київська'
  union
  select id, 'Гоголів' from region where name = 'Київська'
  union
  select id, 'Горенка' from region where name = 'Київська'
  union
  select id, 'Гостомель' from region where name = 'Київська'
  union
  select id, 'Гребінки' from region where name = 'Київська'
  union
  select id, 'Григорівка' from region where name = 'Київська'
  union
  select id, 'Демидов' from region where name = 'Київська'
  union
  select id, 'Денихівка' from region where name = 'Київська'
  union
  select id, 'Димер' from region where name = 'Київська'
  union
  select id, 'Дмитрівка' from region where name = 'Київська'
  union
  select id, 'Дослідницьке' from region where name = 'Київська'
  union
  select id, 'Зазим''я' from region where name = 'Київська'
  union
  select id, 'Згурівка' from region where name = 'Київська'
  union
  select id, 'Кагарлик' from region where name = 'Київська'
  union
  select id, 'Калинівка' from region where name = 'Київська'
  union
  select id, 'Калита' from region where name = 'Київська'
  union
  select id, 'Кашперівка' from region where name = 'Київська'
  union
  select id, 'Київ' from region where name = 'Київська'
  union
  select id, 'Клавдієво-Тарасове' from region where name = 'Київська'
  union
  select id, 'Княжичі' from region where name = 'Київська'
  union
  select id, 'Кодра' from region where name = 'Київська'
  union
  select id, 'Козин' from region where name = 'Київська'
  union
  select id, 'Коцюбинське' from region where name = 'Київська'
  union
  select id, 'Красилівка' from region where name = 'Київська'
  union
  select id, 'Красятичі' from region where name = 'Київська'
  union
  select id, 'Крюківщина' from region where name = 'Київська'
  union
  select id, 'Липівка' from region where name = 'Київська'
  union
  select id, 'Літки' from region where name = 'Київська'
  union
  select id, 'Макаров' from region where name = 'Київська'
  union
  select id, 'Миронівка' from region where name = 'Київська'
  union
  select id, 'Немішаєве' from region where name = 'Київська'
  union
  select id, 'Нові Петрівці' from region where name = 'Київська'
  union
  select id, 'Обухів' from region where name = 'Київська'
  union
  select id, 'Переяслав-Хмельницький' from region where name = 'Київська'
  union
  select id, 'Петропавлівська Борщагівка' from region where name = 'Київська'
  union
  select id, 'Плесецьке' from region where name = 'Київська'
  union
  select id, 'Погреби' from region where name = 'Київська'
  union
  select id, 'Пухівка' from region where name = 'Київська'
  union
  select id, 'Пісківка' from region where name = 'Київська'
  union
  select id, 'Ржищів' from region where name = 'Київська'
  union
  select id, 'Рожни' from region where name = 'Київська'
  union
  select id, 'Рокитне' from region where name = 'Київська'
  union
  select id, 'Росава' from region where name = 'Київська'
  union
  select id, 'Саливонки' from region where name = 'Київська'
  union
  select id, 'Семиполки' from region where name = 'Київська'
  union
  select id, 'Сквира' from region where name = 'Київська'
  union
  select id, 'Славутич' from region where name = 'Київська'
  union
  select id, 'Софіївська Борщагівка' from region where name = 'Київська'
  union
  select id, 'Ставище' from region where name = 'Київська'
  union
  select id, 'Стайки' from region where name = 'Київська'
  union
  select id, 'Старі Петрівці' from region where name = 'Київська'
  union
  select id, 'Стоянка' from region where name = 'Київська'
  union
  select id, 'Тарасівка' from region where name = 'Київська'
  union
  select id, 'Тараща' from region where name = 'Київська'
  union
  select id, 'Тетіїв' from region where name = 'Київська'
  union
  select id, 'Требухів' from region where name = 'Київська'
  union
  select id, 'Узин' from region where name = 'Київська'
  union
  select id, 'Українка' from region where name = 'Київська'
  union
  select id, 'Фастів' from region where name = 'Київська'
  union
  select id, 'Фурси' from region where name = 'Київська'
  union
  select id, 'Ходосівка' from region where name = 'Київська'
  union
  select id, 'Чабани' from region where name = 'Київська'
  union
  select id, 'Чубинське' from region where name = 'Київська'
  union
  select id, 'Шкарівка' from region where name = 'Київська'
  union
  select id, 'Яготин' from region where name = 'Київська'
) t;

-- Кіровоградська
insert into city(region_id, name)
select t.*
from (
  select id, 'Благовіщенське' from region where name = 'Кіровоградська'
  union
  select id, 'Бобринець' from region where name = 'Кіровоградська'
  union
  select id, 'Богданівка' from region where name = 'Кіровоградська'
  union
  select id, 'Власівка' from region where name = 'Кіровоградська'
  union
  select id, 'Вільшанка' from region where name = 'Кіровоградська'
  union
  select id, 'Гайворон' from region where name = 'Кіровоградська'
  union
  select id, 'Голованівськ' from region where name = 'Кіровоградська'
  union
  select id, 'Добровеличківка' from region where name = 'Кіровоградська'
  union
  select id, 'Долинська' from region where name = 'Кіровоградська'
  union
  select id, 'Завалля' from region where name = 'Кіровоградська'
  union
  select id, 'Знам''янка' from region where name = 'Кіровоградська'
  union
  select id, 'Компаніївка' from region where name = 'Кіровоградська'
  union
  select id, 'Кропивницький' from region where name = 'Кіровоградська'
  union
  select id, 'Липняжка' from region where name = 'Кіровоградська'
  union
  select id, 'Мала Виска' from region where name = 'Кіровоградська'
  union
  select id, 'Нова Прага' from region where name = 'Кіровоградська'
  union
  select id, 'Новгородка' from region where name = 'Кіровоградська'
  union
  select id, 'Новоархангельськ' from region where name = 'Кіровоградська'
  union
  select id, 'Новомиргород' from region where name = 'Кіровоградська'
  union
  select id, 'Новоолександрівка' from region where name = 'Кіровоградська'
  union
  select id, 'Новоукраїнка' from region where name = 'Кіровоградська'
  union
  select id, 'Олександрівка' from region where name = 'Кіровоградська'
  union
  select id, 'Олександрія' from region where name = 'Кіровоградська'
  union
  select id, 'Онуфріївка' from region where name = 'Кіровоградська'
  union
  select id, 'Павлиш' from region where name = 'Кіровоградська'
  union
  select id, 'Петрове' from region where name = 'Кіровоградська'
  union
  select id, 'Побузьке' from region where name = 'Кіровоградська'
  union
  select id, 'Помічна' from region where name = 'Кіровоградська'
  union
  select id, 'Рівне' from region where name = 'Кіровоградська'
  union
  select id, 'Світловодськ' from region where name = 'Кіровоградська'
  union
  select id, 'Смоліно' from region where name = 'Кіровоградська'
  union
  select id, 'Суботці' from region where name = 'Кіровоградська'
  union
  select id, 'Трепівка' from region where name = 'Кіровоградська'
  union
  select id, 'Устинівка' from region where name = 'Кіровоградська'
) t;

-- Луганська
insert into city(region_id, name)
select t.*
from (
  select id, 'Біловодськ' from region where name = 'Луганська'
  union
  select id, 'Білокуракине' from region where name = 'Луганська'
  union
  select id, 'Вільхове' from region where name = 'Луганська'
  union
  select id, 'Голубівка' from region where name = 'Луганська'
  union
  select id, 'Гірське' from region where name = 'Луганська'
  union
  select id, 'Золоте' from region where name = 'Луганська'
  union
  select id, 'Кипуче' from region where name = 'Луганська'
  union
  select id, 'Красноріченське' from region where name = 'Луганська'
  union
  select id, 'Кремінна' from region where name = 'Луганська'
  union
  select id, 'Лисичанськ' from region where name = 'Луганська'
  union
  select id, 'Марківка' from region where name = 'Луганська'
  union
  select id, 'Молодогвардійськ' from region where name = 'Луганська'
  union
  select id, 'Мілове' from region where name = 'Луганська'
  union
  select id, 'Нещеретове' from region where name = 'Луганська'
  union
  select id, 'Нижня Шиверівка' from region where name = 'Луганська'
  union
  select id, 'Новоайдар' from region where name = 'Луганська'
  union
  select id, 'Новодружеськ' from region where name = 'Луганська'
  union
  select id, 'Новопсков' from region where name = 'Луганська'
  union
  select id, 'Олександрівськ' from region where name = 'Луганська'
  union
  select id, 'Попасна' from region where name = 'Луганська'
  union
  select id, 'Привілля' from region where name = 'Луганська'
  union
  select id, 'Ровеньки' from region where name = 'Луганська'
  union
  select id, 'Рубіжне' from region where name = 'Луганська'
  union
  select id, 'Сватове' from region where name = 'Луганська'
  union
  select id, 'Слов''яносербськ' from region where name = 'Луганська'
  union
  select id, 'Станично - Луганське' from region where name = 'Луганська'
  union
  select id, 'Старобільськ' from region where name = 'Луганська'
  union
  select id, 'Суходільськ' from region where name = 'Луганська'
  union
  select id, 'Сєвєродонецьк' from region where name = 'Луганська'
  union
  select id, 'Тошківка' from region where name = 'Луганська'
  union
  select id, 'Троїцьке' from region where name = 'Луганська'
  union
  select id, 'Червонопартизанськ' from region where name = 'Луганська'
  union
  select id, 'Щастя' from region where name = 'Луганська'
) t;

-- Львівська
insert into city(region_id, name)
select t.*
from (
  select id, 'Івано-Франкове' from region where name = 'Львівська'
  union
  select id, 'Белз' from region where name = 'Львівська'
  union
  select id, 'Борислав' from region where name = 'Львівська'
  union
  select id, 'Броди' from region where name = 'Львівська'
  union
  select id, 'Брюховичі' from region where name = 'Львівська'
  union
  select id, 'Буськ' from region where name = 'Львівська'
  union
  select id, 'Бібрка' from region where name = 'Львівська'
  union
  select id, 'Великий Любінь' from region where name = 'Львівська'
  union
  select id, 'Великі Мости' from region where name = 'Львівська'
  union
  select id, 'Верхнє Синьовидне' from region where name = 'Львівська'
  union
  select id, 'Винники' from region where name = 'Львівська'
  union
  select id, 'Городок' from region where name = 'Львівська'
  union
  select id, 'Гірник' from region where name = 'Львівська'
  union
  select id, 'Гірське' from region where name = 'Львівська'
  union
  select id, 'Дашава' from region where name = 'Львівська'
  union
  select id, 'Добромиль' from region where name = 'Львівська'
  union
  select id, 'Добротвір' from region where name = 'Львівська'
  union
  select id, 'Добряни' from region where name = 'Львівська'
  union
  select id, 'Дрогобич' from region where name = 'Львівська'
  union
  select id, 'Дубляни' from region where name = 'Львівська'
  union
  select id, 'Жвирка' from region where name = 'Львівська'
  union
  select id, 'Жидачів' from region where name = 'Львівська'
  union
  select id, 'Жовква' from region where name = 'Львівська'
  union
  select id, 'Запитів' from region where name = 'Львівська'
  union
  select id, 'Зимна Вода' from region where name = 'Львівська'
  union
  select id, 'Золочів' from region where name = 'Львівська'
  union
  select id, 'Кам''янка-Бузька' from region where name = 'Львівська'
  union
  select id, 'Комарно' from region where name = 'Львівська'
  union
  select id, 'Краковець' from region where name = 'Львівська'
  union
  select id, 'Куликів' from region where name = 'Львівська'
  union
  select id, 'Лапаївка' from region where name = 'Львівська'
  union
  select id, 'Лопатин' from region where name = 'Львівська'
  union
  select id, 'Львів' from region where name = 'Львівська'
  union
  select id, 'Малехів' from region where name = 'Львівська'
  union
  select id, 'Миколаїв' from region where name = 'Львівська'
  union
  select id, 'Моршин' from region where name = 'Львівська'
  union
  select id, 'Мостиська' from region where name = 'Львівська'
  union
  select id, 'Немирів' from region where name = 'Львівська'
  union
  select id, 'Нижня Яблунька' from region where name = 'Львівська'
  union
  select id, 'Новий Роздол' from region where name = 'Львівська'
  union
  select id, 'Новий Яричів' from region where name = 'Львівська'
  union
  select id, 'Новояворівськ' from region where name = 'Львівська'
  union
  select id, 'Острів' from region where name = 'Львівська'
  union
  select id, 'Перемишляни' from region where name = 'Львівська'
  union
  select id, 'Пустомити' from region where name = 'Львівська'
  union
  select id, 'Підбуж' from region where name = 'Львівська'
  union
  select id, 'Підкамінь' from region where name = 'Львівська'
  union
  select id, 'Рава -Руська' from region where name = 'Львівська'
  union
  select id, 'Радехів' from region where name = 'Львівська'
  union
  select id, 'Рихтичі' from region where name = 'Львівська'
  union
  select id, 'Розвадів' from region where name = 'Львівська'
  union
  select id, 'Рудки' from region where name = 'Львівська'
  union
  select id, 'Рудно' from region where name = 'Львівська'
  union
  select id, 'Самбір' from region where name = 'Львівська'
  union
  select id, 'Сколе' from region where name = 'Львівська'
  union
  select id, 'Славське' from region where name = 'Львівська'
  union
  select id, 'Сокаль' from region where name = 'Львівська'
  union
  select id, 'Сокільники' from region where name = 'Львівська'
  union
  select id, 'Солонка' from region where name = 'Львівська'
  union
  select id, 'Соснівка' from region where name = 'Львівська'
  union
  select id, 'Старий Самбір' from region where name = 'Львівська'
  union
  select id, 'Стебник' from region where name = 'Львівська'
  union
  select id, 'Стрий' from region where name = 'Львівська'
  union
  select id, 'Судова Вишня' from region where name = 'Львівська'
  union
  select id, 'Східниця' from region where name = 'Львівська'
  union
  select id, 'Тростянець' from region where name = 'Львівська'
  union
  select id, 'Трускавець' from region where name = 'Львівська'
  union
  select id, 'Турка' from region where name = 'Львівська'
  union
  select id, 'Угнів' from region where name = 'Львівська'
  union
  select id, 'Ходорів' from region where name = 'Львівська'
  union
  select id, 'Червоноград' from region where name = 'Львівська'
  union
  select id, 'Щирець' from region where name = 'Львівська'
  union
  select id, 'Яворів' from region where name = 'Львівська'
) t;

-- Миколаївська
insert into city(region_id, name)
select t.*
from (
  select id, 'Єланець' from region where name = 'Миколаївська'
  union
  select id, 'Арбузинка' from region where name = 'Миколаївська'
  union
  select id, 'Баловне' from region where name = 'Миколаївська'
  union
  select id, 'Баштанка' from region where name = 'Миколаївська'
  union
  select id, 'Березанка' from region where name = 'Миколаївська'
  union
  select id, 'Березнегувате' from region where name = 'Миколаївська'
  union
  select id, 'Братське' from region where name = 'Миколаївська'
  union
  select id, 'Веселинове' from region where name = 'Миколаївська'
  union
  select id, 'Вознесенськ' from region where name = 'Миколаївська'
  union
  select id, 'Врадіївка' from region where name = 'Миколаївська'
  union
  select id, 'Доманівка' from region where name = 'Миколаївська'
  union
  select id, 'Казанка' from region where name = 'Миколаївська'
  union
  select id, 'Костянтинівка' from region where name = 'Миколаївська'
  union
  select id, 'Криве Озеро' from region where name = 'Миколаївська'
  union
  select id, 'Лимани' from region where name = 'Миколаївська'
  union
  select id, 'Миколаїв' from region where name = 'Миколаївська'
  union
  select id, 'Михайлівка' from region where name = 'Миколаївська'
  union
  select id, 'Мостове' from region where name = 'Миколаївська'
  union
  select id, 'Нова Одеса' from region where name = 'Миколаївська'
  union
  select id, 'Новий Буг' from region where name = 'Миколаївська'
  union
  select id, 'Новопетрівське' from region where name = 'Миколаївська'
  union
  select id, 'Ольшанське' from region where name = 'Миколаївська'
  union
  select id, 'Очаків' from region where name = 'Миколаївська'
  union
  select id, 'Первомайськ' from region where name = 'Миколаївська'
  union
  select id, 'Пересадівка' from region where name = 'Миколаївська'
  union
  select id, 'Прибужжя' from region where name = 'Миколаївська'
  union
  select id, 'Підгородна' from region where name = 'Миколаївська'
  union
  select id, 'Рибаківка' from region where name = 'Миколаївська'
  union
  select id, 'Снігурівка' from region where name = 'Миколаївська'
  union
  select id, 'Южноукраїнськ' from region where name = 'Миколаївська'
) t;

-- Одеська
insert into city(region_id, name)
select t.*
from (
  select id, 'Іванівка' from region where name = 'Одеська'
  union
  select id, 'Ізмаїл' from region where name = 'Одеська'
  union
  select id, 'Іллічівка' from region where name = 'Одеська'
  union
  select id, 'Авангард' from region where name = 'Одеська'
  union
  select id, 'Ананьєв' from region where name = 'Одеська'
  union
  select id, 'Арциз' from region where name = 'Одеська'
  union
  select id, 'Балта' from region where name = 'Одеська'
  union
  select id, 'Березівка' from region where name = 'Одеська'
  union
  select id, 'Болград' from region where name = 'Одеська'
  union
  select id, 'Бурлача Балка' from region where name = 'Одеська'
  union
  select id, 'Білгород-Дністровський' from region where name = 'Одеська'
  union
  select id, 'Біляївка' from region where name = 'Одеська'
  union
  select id, 'Велика Михайлівка' from region where name = 'Одеська'
  union
  select id, 'Великодолинське' from region where name = 'Одеська'
  union
  select id, 'Вилкове' from region where name = 'Одеська'
  union
  select id, 'Доброслав' from region where name = 'Одеська'
  union
  select id, 'Жеребкове' from region where name = 'Одеська'
  union
  select id, 'Затишшя' from region where name = 'Одеська'
  union
  select id, 'Затока' from region where name = 'Одеська'
  union
  select id, 'Кароліно-Бугаз' from region where name = 'Одеська'
  union
  select id, 'Кодима' from region where name = 'Одеська'
  union
  select id, 'Котовка' from region where name = 'Одеська'
  union
  select id, 'Красносілка' from region where name = 'Одеська'
  union
  select id, 'Красні Окни' from region where name = 'Одеська'
  union
  select id, 'Кремидівка' from region where name = 'Одеська'
  union
  select id, 'Крижанівка' from region where name = 'Одеська'
  union
  select id, 'Криничне' from region where name = 'Одеська'
  union
  select id, 'Кілія' from region where name = 'Одеська'
  union
  select id, 'Лиманське' from region where name = 'Одеська'
  union
  select id, 'Любашівка' from region where name = 'Одеська'
  union
  select id, 'Малодолинське' from region where name = 'Одеська'
  union
  select id, 'Маяки' from region where name = 'Одеська'
  union
  select id, 'Миколаївка' from region where name = 'Одеська'
  union
  select id, 'Молодіжне' from region where name = 'Одеська'
  union
  select id, 'Мізікевича' from region where name = 'Одеська'
  union
  select id, 'Нерубайське' from region where name = 'Одеська'
  union
  select id, 'Нова Долина' from region where name = 'Одеська'
  union
  select id, 'Нова Дофінівка' from region where name = 'Одеська'
  union
  select id, 'Овідіополь' from region where name = 'Одеська'
  union
  select id, 'Одеса' from region where name = 'Одеська'
  union
  select id, 'Олександрівка' from region where name = 'Одеська'
  union
  select id, 'Петрівка' from region where name = 'Одеська'
  union
  select id, 'Подольск' from region where name = 'Одеська'
  union
  select id, 'Прилиманське' from region where name = 'Одеська'
  union
  select id, 'Південний' from region where name = 'Одеська'
  union
  select id, 'Рені' from region where name = 'Одеська'
  union
  select id, 'Роздільна' from region where name = 'Одеська'
  union
  select id, 'Саврань' from region where name = 'Одеська'
  union
  select id, 'Сарата' from region where name = 'Одеська'
  union
  select id, 'Сергіївка' from region where name = 'Одеська'
  union
  select id, 'Тарутине' from region where name = 'Одеська'
  union
  select id, 'Татарбунари' from region where name = 'Одеська'
  union
  select id, 'Теплодар' from region where name = 'Одеська'
  union
  select id, 'Усатово' from region where name = 'Одеська'
  union
  select id, 'Фонтанка' from region where name = 'Одеська'
  union
  select id, 'Фрунзівка' from region where name = 'Одеська'
  union
  select id, 'Холодна Балка' from region where name = 'Одеська'
  union
  select id, 'Чорноморськ' from region where name = 'Одеська'
  union
  select id, 'Чорноморське' from region where name = 'Одеська'
  union
  select id, 'Шабо' from region where name = 'Одеська'
  union
  select id, 'Шевченкове' from region where name = 'Одеська'
  union
  select id, 'Ширяєве' from region where name = 'Одеська'
) t;

-- Полтавська
insert into city(region_id, name)
select t.*
from (
  select id, 'Бутенки' from region where name = 'Полтавська'
  union
  select id, 'Білики' from region where name = 'Полтавська'
  union
  select id, 'Велика Багачка' from region where name = 'Полтавська'
  union
  select id, 'Гадяч' from region where name = 'Полтавська'
  union
  select id, 'Глобине' from region where name = 'Полтавська'
  union
  select id, 'Гоголеве' from region where name = 'Полтавська'
  union
  select id, 'Горішні Плавні' from region where name = 'Полтавська'
  union
  select id, 'Градизьк' from region where name = 'Полтавська'
  union
  select id, 'Гребінка' from region where name = 'Полтавська'
  union
  select id, 'Диканька' from region where name = 'Полтавська'
  union
  select id, 'Залізничне' from region where name = 'Полтавська'
  union
  select id, 'Зіньків' from region where name = 'Полтавська'
  union
  select id, 'Карлівка' from region where name = 'Полтавська'
  union
  select id, 'Кобеляки' from region where name = 'Полтавська'
  union
  select id, 'Козельщина' from region where name = 'Полтавська'
  union
  select id, 'Котельва' from region where name = 'Полтавська'
  union
  select id, 'Кременчук' from region where name = 'Полтавська'
  union
  select id, 'Ланна' from region where name = 'Полтавська'
  union
  select id, 'Лохвиця' from region where name = 'Полтавська'
  union
  select id, 'Лубни' from region where name = 'Полтавська'
  union
  select id, 'Машівка' from region where name = 'Полтавська'
  union
  select id, 'Миргород' from region where name = 'Полтавська'
  union
  select id, 'Нові Санжари' from region where name = 'Полтавська'
  union
  select id, 'Опішня' from region where name = 'Полтавська'
  union
  select id, 'Оржиця' from region where name = 'Полтавська'
  union
  select id, 'Пирятин' from region where name = 'Полтавська'
  union
  select id, 'Полтава' from region where name = 'Полтавська'
  union
  select id, 'Решетилівка' from region where name = 'Полтавська'
  union
  select id, 'Розсошенці' from region where name = 'Полтавська'
  union
  select id, 'Семенівка' from region where name = 'Полтавська'
  union
  select id, 'Терешки' from region where name = 'Полтавська'
  union
  select id, 'Хорол' from region where name = 'Полтавська'
  union
  select id, 'Червонозаводське' from region where name = 'Полтавська'
  union
  select id, 'Чорнухи' from region where name = 'Полтавська'
  union
  select id, 'Чутове' from region where name = 'Полтавська'
  union
  select id, 'Шишаки' from region where name = 'Полтавська'
) t;

-- Рівненська
insert into city(region_id, name)
select t.*
from (
  select id, 'Антонівка' from region where name = 'Рівненська'
  union
  select id, 'Березне' from region where name = 'Рівненська'
  union
  select id, 'Бугрин' from region where name = 'Рівненська'
  union
  select id, 'Вараш' from region where name = 'Рівненська'
  union
  select id, 'Варковичі' from region where name = 'Рівненська'
  union
  select id, 'Висоцьк' from region where name = 'Рівненська'
  union
  select id, 'Володимирець' from region where name = 'Рівненська'
  union
  select id, 'Гоща' from region where name = 'Рівненська'
  union
  select id, 'Демидівка' from region where name = 'Рівненська'
  union
  select id, 'Деражне' from region where name = 'Рівненська'
  union
  select id, 'Дубно' from region where name = 'Рівненська'
  union
  select id, 'Дубровиця' from region where name = 'Рівненська'
  union
  select id, 'Зарічне' from region where name = 'Рівненська'
  union
  select id, 'Здолбунів' from region where name = 'Рівненська'
  union
  select id, 'Зоря' from region where name = 'Рівненська'
  union
  select id, 'Зірне' from region where name = 'Рівненська'
  union
  select id, 'Квасилів' from region where name = 'Рівненська'
  union
  select id, 'Клевань' from region where name = 'Рівненська'
  union
  select id, 'Клесів' from region where name = 'Рівненська'
  union
  select id, 'Козин' from region where name = 'Рівненська'
  union
  select id, 'Корець' from region where name = 'Рівненська'
  union
  select id, 'Костопіль' from region where name = 'Рівненська'
  union
  select id, 'Млинів' from region where name = 'Рівненська'
  union
  select id, 'Моквин' from region where name = 'Рівненська'
  union
  select id, 'Мізоч' from region where name = 'Рівненська'
  union
  select id, 'Оженин' from region where name = 'Рівненська'
  union
  select id, 'Оржів' from region where name = 'Рівненська'
  union
  select id, 'Острог' from region where name = 'Рівненська'
  union
  select id, 'Острожець' from region where name = 'Рівненська'
  union
  select id, 'Радивилів' from region where name = 'Рівненська'
  union
  select id, 'Рафалівка' from region where name = 'Рівненська'
  union
  select id, 'Рокитне' from region where name = 'Рівненська'
  union
  select id, 'Рівне' from region where name = 'Рівненська'
  union
  select id, 'Сарни' from region where name = 'Рівненська'
  union
  select id, 'Сарни' from region where name = 'Рівненська'
  union
  select id, 'Смига' from region where name = 'Рівненська'
  union
  select id, 'Соснове' from region where name = 'Рівненська'
  union
  select id, 'Степань' from region where name = 'Рівненська'
  union
  select id, 'Томашгород' from region where name = 'Рівненська'
) t;

-- Сумська
insert into city(region_id, name)
select t.*
from (
  select id, 'Боромля' from region where name = 'Сумська'
  union
  select id, 'Буринь' from region where name = 'Сумська'
  union
  select id, 'Білопілля' from region where name = 'Сумська'
  union
  select id, 'Велика Писарівка' from region where name = 'Сумська'
  union
  select id, 'Ворожба' from region where name = 'Сумська'
  union
  select id, 'Вільшана' from region where name = 'Сумська'
  union
  select id, 'Глухів' from region where name = 'Сумська'
  union
  select id, 'Дружба' from region where name = 'Сумська'
  union
  select id, 'Дубов''язівка' from region where name = 'Сумська'
  union
  select id, 'Жовтневе' from region where name = 'Сумська'
  union
  select id, 'Конотоп' from region where name = 'Сумська'
  union
  select id, 'Краснопілля' from region where name = 'Сумська'
  union
  select id, 'Кролевець' from region where name = 'Сумська'
  union
  select id, 'Лебедин' from region where name = 'Сумська'
  union
  select id, 'Липова Долина' from region where name = 'Сумська'
  union
  select id, 'Недригайлів' from region where name = 'Сумська'
  union
  select id, 'Охтирка' from region where name = 'Сумська'
  union
  select id, 'Путивль' from region where name = 'Сумська'
  union
  select id, 'Ромни' from region where name = 'Сумська'
  union
  select id, 'Свеса' from region where name = 'Сумська'
  union
  select id, 'Середина-Буда' from region where name = 'Сумська'
  union
  select id, 'Суми' from region where name = 'Сумська'
  union
  select id, 'Терни' from region where name = 'Сумська'
  union
  select id, 'Тростянець' from region where name = 'Сумська'
  union
  select id, 'Шостка' from region where name = 'Сумська'
  union
  select id, 'Ямпіль' from region where name = 'Сумська'
) t;

-- Тернопільська
insert into city(region_id, name)
select t.*
from (
  select id, 'Бережани' from region where name = 'Тернопільська'
  union
  select id, 'Борщів' from region where name = 'Тернопільська'
  union
  select id, 'Бучач' from region where name = 'Тернопільська'
  union
  select id, 'Великі Бірки' from region where name = 'Тернопільська'
  union
  select id, 'Великі Дедеркали' from region where name = 'Тернопільська'
  union
  select id, 'Вишнівець' from region where name = 'Тернопільська'
  union
  select id, 'Гримайлів' from region where name = 'Тернопільська'
  union
  select id, 'Гусятин' from region where name = 'Тернопільська'
  union
  select id, 'Заводське' from region where name = 'Тернопільська'
  union
  select id, 'Залізці' from region where name = 'Тернопільська'
  union
  select id, 'Заліщики' from region where name = 'Тернопільська'
  union
  select id, 'Збараж' from region where name = 'Тернопільська'
  union
  select id, 'Зборів' from region where name = 'Тернопільська'
  union
  select id, 'Золотий Потік' from region where name = 'Тернопільська'
  union
  select id, 'Золотники' from region where name = 'Тернопільська'
  union
  select id, 'Козова' from region where name = 'Тернопільська'
  union
  select id, 'Копичинці' from region where name = 'Тернопільська'
  union
  select id, 'Коропець' from region where name = 'Тернопільська'
  union
  select id, 'Косів' from region where name = 'Тернопільська'
  union
  select id, 'Кременець' from region where name = 'Тернопільська'
  union
  select id, 'Ланівці' from region where name = 'Тернопільська'
  union
  select id, 'Мельниця-Подільська' from region where name = 'Тернопільська'
  union
  select id, 'Микулинці' from region where name = 'Тернопільська'
  union
  select id, 'Монастириська' from region where name = 'Тернопільська'
  union
  select id, 'Почаїв' from region where name = 'Тернопільська'
  union
  select id, 'Підвисоке' from region where name = 'Тернопільська'
  union
  select id, 'Підволочиськ' from region where name = 'Тернопільська'
  union
  select id, 'Підгайці' from region where name = 'Тернопільська'
  union
  select id, 'Підгородне' from region where name = 'Тернопільська'
  union
  select id, 'Скала-Подільська' from region where name = 'Тернопільська'
  union
  select id, 'Скалат' from region where name = 'Тернопільська'
  union
  select id, 'Теребовля' from region where name = 'Тернопільська'
  union
  select id, 'Тернопіль' from region where name = 'Тернопільська'
  union
  select id, 'Товсте' from region where name = 'Тернопільська'
  union
  select id, 'Хоростків' from region where name = 'Тернопільська'
  union
  select id, 'Чортків' from region where name = 'Тернопільська'
  union
  select id, 'Шумськ' from region where name = 'Тернопільська'
) t;

-- Харківська
insert into city(region_id, name)
select t.*
from (
  select id, 'Ізюм' from region where name = 'Харківська'
  union
  select id, 'Бабаї' from region where name = 'Харківська'
  union
  select id, 'Балаклея' from region where name = 'Харківська'
  union
  select id, 'Барвінкове' from region where name = 'Харківська'
  union
  select id, 'Безлюдівка' from region where name = 'Харківська'
  union
  select id, 'Березівка' from region where name = 'Харківська'
  union
  select id, 'Близнюки' from region where name = 'Харківська'
  union
  select id, 'Богодухів' from region where name = 'Харківська'
  union
  select id, 'Борова' from region where name = 'Харківська'
  union
  select id, 'Буди' from region where name = 'Харківська'
  union
  select id, 'Валки' from region where name = 'Харківська'
  union
  select id, 'Васищево' from region where name = 'Харківська'
  union
  select id, 'Великий Бурлук' from region where name = 'Харківська'
  union
  select id, 'Високий' from region where name = 'Харківська'
  union
  select id, 'Вовчанськ' from region where name = 'Харківська'
  union
  select id, 'Вільшани' from region where name = 'Харківська'
  union
  select id, 'Дачне' from region where name = 'Харківська'
  union
  select id, 'Дворічна' from region where name = 'Харківська'
  union
  select id, 'Дергачі' from region where name = 'Харківська'
  union
  select id, 'Есхар' from region where name = 'Харківська'
  union
  select id, 'Зачепилівка' from region where name = 'Харківська'
  union
  select id, 'Зміїв' from region where name = 'Харківська'
  union
  select id, 'Золочів' from region where name = 'Харківська'
  union
  select id, 'Зідьки' from region where name = 'Харківська'
  union
  select id, 'Капитолівка' from region where name = 'Харківська'
  union
  select id, 'Кегичівка' from region where name = 'Харківська'
  union
  select id, 'Ков''яги' from region where name = 'Харківська'
  union
  select id, 'Козача Лопань' from region where name = 'Харківська'
  union
  select id, 'Коломак' from region where name = 'Харківська'
  union
  select id, 'Коротич' from region where name = 'Харківська'
  union
  select id, 'Красноград' from region where name = 'Харківська'
  union
  select id, 'Краснокутськ' from region where name = 'Харківська'
  union
  select id, 'Краснопавлівка' from region where name = 'Харківська'
  union
  select id, 'Кулиничі' from region where name = 'Харківська'
  union
  select id, 'Куп''янськ' from region where name = 'Харківська'
  union
  select id, 'Ківшарівка' from region where name = 'Харківська'
  union
  select id, 'Лиман' from region where name = 'Харківська'
  union
  select id, 'Липці' from region where name = 'Харківська'
  union
  select id, 'Лозова' from region where name = 'Харківська'
  union
  select id, 'Люботин' from region where name = 'Харківська'
  union
  select id, 'Мала Данилівка' from region where name = 'Харківська'
  union
  select id, 'Малинівка' from region where name = 'Харківська'
  union
  select id, 'Мерефа' from region where name = 'Харківська'
  union
  select id, 'Мурафа' from region where name = 'Харківська'
  union
  select id, 'Нова Водолага' from region where name = 'Харківська'
  union
  select id, 'Панютине' from region where name = 'Харківська'
  union
  select id, 'Пархомівка' from region where name = 'Харківська'
  union
  select id, 'Первомайський' from region where name = 'Харківська'
  union
  select id, 'Пересічна' from region where name = 'Харківська'
  union
  select id, 'Печеніги' from region where name = 'Харківська'
  union
  select id, 'Покотилівка' from region where name = 'Харківська'
  union
  select id, 'Прудянка' from region where name = 'Харківська'
  union
  select id, 'Пісочин' from region where name = 'Харківська'
  union
  select id, 'Рогань' from region where name = 'Харківська'
  union
  select id, 'Сахновщина' from region where name = 'Харківська'
  union
  select id, 'Слобожанське' from region where name = 'Харківська'
  union
  select id, 'Солоницівка' from region where name = 'Харківська'
  union
  select id, 'Старий Мерчик' from region where name = 'Харківська'
  union
  select id, 'Старий Салтов' from region where name = 'Харківська'
  union
  select id, 'Старовірівка' from region where name = 'Харківська'
  union
  select id, 'Харків' from region where name = 'Харківська'
  union
  select id, 'Червоний Донець' from region where name = 'Харківська'
  union
  select id, 'Чкаловське' from region where name = 'Харківська'
  union
  select id, 'Чугуїв' from region where name = 'Харківська'
  union
  select id, 'Шевченкове' from region where name = 'Харківська'
) t;

-- Херсонська
insert into city(region_id, name)
select t.*
from (
  select id, 'Іванівка' from region where name = 'Херсонська'
  union
  select id, 'Антонівка' from region where name = 'Херсонська'
  union
  select id, 'Асканія-Нова' from region where name = 'Херсонська'
  union
  select id, 'Берислав' from region where name = 'Херсонська'
  union
  select id, 'Білозерка' from region where name = 'Херсонська'
  union
  select id, 'Велика Лепетиха' from region where name = 'Херсонська'
  union
  select id, 'Велика Олександрівка' from region where name = 'Херсонська'
  union
  select id, 'Великі Копані' from region where name = 'Херсонська'
  union
  select id, 'Верхній Рогачик' from region where name = 'Херсонська'
  union
  select id, 'Високопілля' from region where name = 'Херсонська'
  union
  select id, 'Гаврилівка' from region where name = 'Херсонська'
  union
  select id, 'Генічеськ' from region where name = 'Херсонська'
  union
  select id, 'Гола Пристань' from region where name = 'Херсонська'
  union
  select id, 'Горностаївка' from region where name = 'Херсонська'
  union
  select id, 'Каланчак' from region where name = 'Херсонська'
  union
  select id, 'Каховка' from region where name = 'Херсонська'
  union
  select id, 'Нижні Сірогози' from region where name = 'Херсонська'
  union
  select id, 'Нова Збур''ївка' from region where name = 'Херсонська'
  union
  select id, 'Нова Каховка' from region where name = 'Херсонська'
  union
  select id, 'Нововоронцовка' from region where name = 'Херсонська'
  union
  select id, 'Новоолексіївка' from region where name = 'Херсонська'
  union
  select id, 'Новотроїцьке' from region where name = 'Херсонська'
  union
  select id, 'Олешки' from region where name = 'Херсонська'
  union
  select id, 'Скадовськ' from region where name = 'Херсонська'
  union
  select id, 'Таврійськ' from region where name = 'Херсонська'
  union
  select id, 'Херсон' from region where name = 'Херсонська'
  union
  select id, 'Чаплинка' from region where name = 'Херсонська'
) t;

-- Хмельницька
insert into city(region_id, name)
select t.*
from (
  select id, 'Ізяслав' from region where name = 'Хмельницька'
  union
  select id, 'Білогір''я' from region where name = 'Хмельницька'
  union
  select id, 'Волочиськ' from region where name = 'Хмельницька'
  union
  select id, 'Війтівці' from region where name = 'Хмельницька'
  union
  select id, 'Віньківці' from region where name = 'Хмельницька'
  union
  select id, 'Городок' from region where name = 'Хмельницька'
  union
  select id, 'Гриців' from region where name = 'Хмельницька'
  union
  select id, 'Деражня' from region where name = 'Хмельницька'
  union
  select id, 'Довжок' from region where name = 'Хмельницька'
  union
  select id, 'Дунаївці' from region where name = 'Хмельницька'
  union
  select id, 'Кам''янець-Подільський' from region where name = 'Хмельницька'
  union
  select id, 'Красилів' from region where name = 'Хмельницька'
  union
  select id, 'Летичів' from region where name = 'Хмельницька'
  union
  select id, 'Миколаїв' from region where name = 'Хмельницька'
  union
  select id, 'Нетішин' from region where name = 'Хмельницька'
  union
  select id, 'Нова Ушиця' from region where name = 'Хмельницька'
  union
  select id, 'Полонне' from region where name = 'Хмельницька'
  union
  select id, 'Понінка' from region where name = 'Хмельницька'
  union
  select id, 'Сатанів' from region where name = 'Хмельницька'
  union
  select id, 'Славута' from region where name = 'Хмельницька'
  union
  select id, 'Стара Синява' from region where name = 'Хмельницька'
  union
  select id, 'Старокостянтинів' from region where name = 'Хмельницька'
  union
  select id, 'Теофіполь' from region where name = 'Хмельницька'
  union
  select id, 'Хмельницький' from region where name = 'Хмельницька'
  union
  select id, 'Чемерівці' from region where name = 'Хмельницька'
  union
  select id, 'Шепетівка' from region where name = 'Хмельницька'
  union
  select id, 'Ямпіль' from region where name = 'Хмельницька'
  union
  select id, 'Ярмолинці' from region where name = 'Хмельницька'
) t;

-- Черкаська
insert into city(region_id, name)
select t.*
from (
  select id, 'Єрки' from region where name = 'Черкаська'
  union
  select id, 'Бабанка' from region where name = 'Черкаська'
  union
  select id, 'Байбузи' from region where name = 'Черкаська'
  union
  select id, 'Білозір''я' from region where name = 'Черкаська'
  union
  select id, 'Валява' from region where name = 'Черкаська'
  union
  select id, 'Васильків' from region where name = 'Черкаська'
  union
  select id, 'Ватутіне' from region where name = 'Черкаська'
  union
  select id, 'Вергуни' from region where name = 'Черкаська'
  union
  select id, 'Верхнячка' from region where name = 'Черкаська'
  union
  select id, 'Вознесенське' from region where name = 'Черкаська'
  union
  select id, 'Вільшана' from region where name = 'Черкаська'
  union
  select id, 'Гельмязів' from region where name = 'Черкаська'
  union
  select id, 'Городище' from region where name = 'Черкаська'
  union
  select id, 'Драбів' from region where name = 'Черкаська'
  union
  select id, 'Дубіївка' from region where name = 'Черкаська'
  union
  select id, 'Жашків' from region where name = 'Черкаська'
  union
  select id, 'Звенигородка' from region where name = 'Черкаська'
  union
  select id, 'Золотоноша' from region where name = 'Черкаська'
  union
  select id, 'Кам''янка' from region where name = 'Черкаська'
  union
  select id, 'Канів' from region where name = 'Черкаська'
  union
  select id, 'Катеринопіль' from region where name = 'Черкаська'
  union
  select id, 'Корсунь-Шевченківський' from region where name = 'Черкаська'
  union
  select id, 'Ладижинка' from region where name = 'Черкаська'
  union
  select id, 'Лисянка' from region where name = 'Черкаська'
  union
  select id, 'Маньківка' from region where name = 'Черкаська'
  union
  select id, 'Матусів' from region where name = 'Черкаська'
  union
  select id, 'Мліїв' from region where name = 'Черкаська'
  union
  select id, 'Монастирище' from region where name = 'Черкаська'
  union
  select id, 'Рацево' from region where name = 'Черкаська'
  union
  select id, 'Російська Поляна' from region where name = 'Черкаська'
  union
  select id, 'Селище' from region where name = 'Черкаська'
  union
  select id, 'Сміла' from region where name = 'Черкаська'
  union
  select id, 'Стеблів' from region where name = 'Черкаська'
  union
  select id, 'Тальне' from region where name = 'Черкаська'
  union
  select id, 'Умань' from region where name = 'Черкаська'
  union
  select id, 'Хлистунівка' from region where name = 'Черкаська'
  union
  select id, 'Христинівка' from region where name = 'Черкаська'
  union
  select id, 'Червона Слобода' from region where name = 'Черкаська'
  union
  select id, 'Черкаси' from region where name = 'Черкаська'
  union
  select id, 'Чигирин' from region where name = 'Черкаська'
  union
  select id, 'Чорнобай' from region where name = 'Черкаська'
  union
  select id, 'Шпола' from region where name = 'Черкаська'
  union
  select id, 'Шрамківка' from region where name = 'Черкаська'
) t;

-- Чернігівська
insert into city(region_id, name)
select t.*
from (
  select id, 'Ічня' from region where name = 'Чернігівська'
  union
  select id, 'Батурин' from region where name = 'Чернігівська'
  union
  select id, 'Бахмач' from region where name = 'Чернігівська'
  union
  select id, 'Березна' from region where name = 'Чернігівська'
  union
  select id, 'Бобровиця' from region where name = 'Чернігівська'
  union
  select id, 'Борзна' from region where name = 'Чернігівська'
  union
  select id, 'Варва' from region where name = 'Чернігівська'
  union
  select id, 'Вертіївка' from region where name = 'Чернігівська'
  union
  select id, 'Городня' from region where name = 'Чернігівська'
  union
  select id, 'Десна' from region where name = 'Чернігівська'
  union
  select id, 'Киїнка' from region where name = 'Чернігівська'
  union
  select id, 'Козелець' from region where name = 'Чернігівська'
  union
  select id, 'Короп' from region where name = 'Чернігівська'
  union
  select id, 'Корюківка' from region where name = 'Чернігівська'
  union
  select id, 'Куликівка' from region where name = 'Чернігівська'
  union
  select id, 'Ладан' from region where name = 'Чернігівська'
  union
  select id, 'Лосинівка' from region where name = 'Чернігівська'
  union
  select id, 'Мена' from region where name = 'Чернігівська'
  union
  select id, 'Новгород-Сіверський' from region where name = 'Чернігівська'
  union
  select id, 'Новий Биків' from region where name = 'Чернігівська'
  union
  select id, 'Новоселівка' from region where name = 'Чернігівська'
  union
  select id, 'Носівка' from region where name = 'Чернігівська'
  union
  select id, 'Ніжин' from region where name = 'Чернігівська'
  union
  select id, 'Остер' from region where name = 'Чернігівська'
  union
  select id, 'Парафіївка' from region where name = 'Чернігівська'
  union
  select id, 'Прилуки' from region where name = 'Чернігівська'
  union
  select id, 'Ріпки' from region where name = 'Чернігівська'
  union
  select id, 'Семенівка' from region where name = 'Чернігівська'
  union
  select id, 'Сеньківка' from region where name = 'Чернігівська'
  union
  select id, 'Сновськ' from region where name = 'Чернігівська'
  union
  select id, 'Сосниця' from region where name = 'Чернігівська'
  union
  select id, 'Срібне' from region where name = 'Чернігівська'
  union
  select id, 'Талалаївка' from region where name = 'Чернігівська'
  union
  select id, 'Чернігів' from region where name = 'Чернігівська'
) t;

-- Чернівецька
insert into city(region_id, name)
select t.*
from (
  select id, 'Берегомет' from region where name = 'Чернівецька'
  union
  select id, 'Вашківці' from region where name = 'Чернівецька'
  union
  select id, 'Вижниця' from region where name = 'Чернівецька'
  union
  select id, 'Вороновиця' from region where name = 'Чернівецька'
  union
  select id, 'Герца' from region where name = 'Чернівецька'
  union
  select id, 'Глибока' from region where name = 'Чернівецька'
  union
  select id, 'Зарожани' from region where name = 'Чернівецька'
  union
  select id, 'Заставна' from region where name = 'Чернівецька'
  union
  select id, 'Кельменці' from region where name = 'Чернівецька'
  union
  select id, 'Клішківці' from region where name = 'Чернівецька'
  union
  select id, 'Кострижівка' from region where name = 'Чернівецька'
  union
  select id, 'Кіцмань' from region where name = 'Чернівецька'
  union
  select id, 'Лужани' from region where name = 'Чернівецька'
  union
  select id, 'Мамалига' from region where name = 'Чернівецька'
  union
  select id, 'Мамаївці' from region where name = 'Чернівецька'
  union
  select id, 'Неполоківці' from region where name = 'Чернівецька'
  union
  select id, 'Новодністровськ' from region where name = 'Чернівецька'
  union
  select id, 'Новоселиця' from region where name = 'Чернівецька'
  union
  select id, 'Путила' from region where name = 'Чернівецька'
  union
  select id, 'Сокиряни' from region where name = 'Чернівецька'
  union
  select id, 'Сторожинець' from region where name = 'Чернівецька'
  union
  select id, 'Хотин' from region where name = 'Чернівецька'
  union
  select id, 'Чагор' from region where name = 'Чернівецька'
  union
  select id, 'Чернівці' from region where name = 'Чернівецька'
) t;