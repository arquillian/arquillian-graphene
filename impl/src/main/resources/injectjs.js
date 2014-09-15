var s = document.createElement('script');
s.type = 'text/javascript';
s.src = arguments[0];
console.log('injecting ' + s.src);
document.body.appendChild(s);