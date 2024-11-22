# Online Library

**Цел на проекта**: Да се разработи уеб приложение на Java, което предоставя функционалност за управление на онлайн библиотека

"**Потребител**" – обикновен потребител, който може да търси и заема книги  
"**Администратор**" – може да добавя, редактира и изтрива книги

**Приложението трябва да позволявa:**
  - администраторите:
    - да добавят книги
    - да управляват книги
  - потребителите:
    - да търсят книги
    - да заемат книги
    - да връщат книги

**Регистрация и автентикация:**
  - Потребителите:
    - Да могат да се регистрират с (имейл, парола и роля)
  - Реализирайте вход и изход.

**Управление на книги (администратор):**
  - Да добавят нови книги
    - Атрибути:
      - Заглавие
      - Автор
      - Жанр
      - Година на публикуване
      - Брой налични копия
  - Да редактират информацията за съществуващи книги
  - Да изтриват книги

**Търсене и разглеждане на книги (потребител):**
  - Търсят книги по (заглавие/автор/жанр)
  - Разглеждат списък с налични книги
  - Преглеждат детайли за книга (включително броя на наличните копия)

**Заемане и връщане на книги:**
  - Потребителите могат да заемат книги, ако има налични копия
  - Всяка заета книга трябва да има срок на връщане (напр. 14 дни)
  - Потребителите могат да връщат книги
  - Администраторите могат да виждат списък със заети книги и техните срокове

**Уведомления:**
  - Реализирайте механизъм за уведомяване на потребителите, когато срокът за връщане на книга изтича (напр. 3 дни преди крайния срок)

**Статистика (администратор):**
  - Най-популярните книги (според броя заеми)
  - Общ брой на потребители и техния статус (активни/неактивни)
