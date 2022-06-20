fetch("https://zsdp.govt.hu:5000/api.login", {
  "headers": {
    "accept": "application/json, text/plain, */*",
    "access-control-allow-credentials": "true",
    "access-control-allow-headers": "*",
    "access-control-allow-methods": "*",
    "access-control-allow-origin": "*",
    "content-type": "application/json",
    "Referer": "http://localhost:3000/",
    "Referrer-Policy": "strict-origin-when-cross-origin"
  },
  "body": "{\"user\":\"morad\",\"pass\":\"morad\"}",
  "method": "POST"
}).then(r => console.log(r))

  //.then(d => console.log(d))
  .catch(e => console.log(e));
