<h1>Если кто-то будет писать после меня<h1>
1)Аутентификации 
  в AuthController нет эндпоинта для логина, потому что он в securityConfig через filterChain (.login). 
  Так же поэтому в тесте AuthControllerIT shouldLoginUser через MockMvc, а не через ResponseEntity.
  ПОТОМУ ЧТО в /api/v1/auth/login  Spring Security ВОЗВРАЩАЕТ application/octet-stream, а не application/json.
  ПОЭТОМУ ЕСЛИ БУДЕШЬ ТЕСТИТЬ ЭНДПОИНТЫ В postman, ТО ВСТАВЛЯЙ username И password НЕ В row, А В x-www-form-urlencoded(key, value)!!!
2) Изначально был postgres, но из-за того, что он не находил существующие значения в самой субд по имени или мылу(хотя были данные), то было принято решение перейти на mysql, если вдруг понадобиться миграция на Postgres, то не забудь generationType в айдишниках на SEQUENCE поменять!!!
  
