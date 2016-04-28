// Stéphane Raux @ Linkfluence                                                                                                         
// (requires sigma.js to be loaded)

//sigma.publicPrototype.drawImage = function(bgColor,cb) {
//
//  var canvas = document.createElement('canvas');
//  canvas.setAttribute('width', sigma.width + 'px');
//  canvas.setAttribute('height', sigma.height + 'px');
//
//  var ctx = canvas.getContext('2d');
//  if(bgColor) {
//    ctx.fillStyle = bgColor;
//    ctx.fillRect(0,0,sigma.width,sigma.height);
//  }
//  var layers = ['edges','nodes','labels','hover'];
//  for( var id in layers ) {
//    ctx.drawImage(sigma.domElements[layers[id]],0,0);
//  }
//  if(cb) {
//    cb(ctx);
//  }
//  return canvas.toDataURL();
//};
