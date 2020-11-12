/* ******************************************************
 * CPA - Conception et pratique de l'algorithmique.
 * Copyright (C) 2014 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: RBB_collision.js 2014-01-28 buixuan $
 * ******************************************************/

//--------------------------//
// INIT SOME CONFIGURATIONS //
//--------------------------//
var context=main_window.getContext('2d');
var width=main_window.width;
var height=main_window.height;
var friction=0.98;
var red = new player(0x51E77E,
                     new avatar(3*width/4,height/2,height/8,"#FF0000","#FF2400"),
                     new keys(0x25,0x27,0x26,0x28)
                    );
var blue = new player(0xED1C7E,
                      new avatar(width/4,height/2,height/8,"#0000FF","#0066FF"),
                      new keys(0x51,0x44,0x5A,0x53)
                     );
var black = new player(0xBADDAD,
                       new avatar(width/2,height/2,height/4.5,"#000000","#505050"),
                       new keys(0x00,0x00,0x00,0x00)
                      );
var goal = {x:width-blue.avatar.radius,y:height/3,
            width:width-blue.avatar.radius/2,height:height/3};
var players = new Array(red,blue,black);
//--------------------------//
// END OF CONFIGURATIONS    //
//--------------------------//

function avatar(x,y,r,c,bc){
  this.name="CIRCLE";
  this.x=x;
  this.y=y;
  this.radius=r;
  this.color=c;
  this.bordercolor=bc;
}

function keys(l,r,u,d){
  this.left={code:l,hold:false};
  this.right={code:r,hold:false};
  this.up={code:u,hold:false};
  this.down={code:d,hold:false};
}

function player(id, avatar, keys){
  this.id=id;
  this.avatar=avatar;
  this.keys=keys;
  this.vx=0;
  this.vy=0;

  this.updateFriction=updateFriction;
  function updateFriction(){ this.vx*=friction; this.vy*=friction; }

  this.updateCommands=updateCommands;
  function updateCommands(){ 
    if(this.keys.left.hold){this.vx--;}
    if(this.keys.right.hold){this.vx++;}
    if(this.keys.up.hold){this.vy--;}
    if(this.keys.down.hold){this.vy++;}
  }

  this.updateCollisionBorder=updateCollisionBorder;
  function updateCollisionBorder(){ 
    if (collisionLeftBorder(avatar)){ this.vx*=-1; this.avatar.x=this.avatar.radius; return true; }
    if (collisionRightBorder(avatar)){ this.vx*=-1; this.avatar.x=width-this.avatar.radius; return true; }
    if (collisionTopBorder(avatar)){ this.vy*=-1; this.avatar.y=this.avatar.radius; return true; }
    if (collisionBottomBorder(avatar)){ this.vy*=-1; this.avatar.y=height-this.avatar.radius; return true; }
    return false;
  }
  
  this.updateCollisionSameMass=updateCollisionSameMass;
  function updateCollisionSameMass(otherPlayer){
    if(collisionCircles(this.avatar, otherPlayer.avatar)){
      var x1=this.avatar.x;
      var y1=this.avatar.y;
      var r1=this.avatar.radius;
      var x2=otherPlayer.avatar.x;
      var y2=otherPlayer.avatar.y;
      var r2=otherPlayer.avatar.radius;
      var d=Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
      var nx = (x2 - x1)/(r1+r2);
      var ny = (y2 - y1)/(r1+r2);
      var gx = -ny;
      var gy = nx;
      var v1n = nx*this.vx + ny*this.vy;
      var v1g = gx*this.vx + gy*this.vy;
      var v2n = nx*otherPlayer.vx + ny*otherPlayer.vy;
      var v2g = gx*otherPlayer.vx + gy*otherPlayer.vy;
      this.vx = nx*v2n +  gx*v1g;
      this.vy = ny*v2n +  gy*v1g;
      otherPlayer.vx = nx*v1n +  gx*v2g;
      otherPlayer.vy = ny*v1n +  gy*v2g;
 
      otherPlayer.avatar.x = x1 + (r1+r2)*(x2-x1)/d;
      otherPlayer.avatar.y = y1 + (r1+r2)*(y2-y1)/d;
      return true;
    }
    return false;
  }

  this.updateCollisionInfiniteMass=updateCollisionInfiniteMass;
  function updateCollisionInfiniteMass(otherPlayer){
    if(collisionCircles(this.avatar,otherPlayer.avatar)){
      var x1=otherPlayer.avatar.x;
      var y1=otherPlayer.avatar.y;
      var r1=otherPlayer.avatar.radius;
      var x2=this.avatar.x;
      var y2=this.avatar.y;
      var r2=this.avatar.radius;
      var d=Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
      var nx = (x2 - x1)/(r1+r2);
      var ny = (y2 - y1)/(r1+r2);
      var pthis = this.vx*nx+this.vy*ny;
      this.vx = this.vx - 2*pthis*nx;
      this.vy = this.vy - 2*pthis*ny;
 
      this.avatar.x = x1 + (r1+r2)*(x2-x1)/d;
      this.avatar.y = y1 + (r1+r2)*(y2-y1)/d;
      return true;
    }
    return false;
  }

  this.updatePosition=updatePosition;
  function updatePosition(){ this.avatar.x+=this.vx; this.avatar.y+=this.vy; }
 
  this.draw=draw;
  function draw(){
    context.beginPath();
    var g=context.createRadialGradient(this.avatar.x,this.avatar.y,this.avatar.radius*0.98,this.avatar.x,this.avatar.y,this.avatar.radius);
    g.addColorStop(0,this.avatar.color);
    g.addColorStop(1,this.avatar.bordercolor);
    context.fillStyle=g;
    context.arc(this.avatar.x,this.avatar.y,
                this.avatar.radius,0,Math.PI*2,true);
    context.fill();
    context.closePath();
  }
}

document.onkeydown = function(event) {
  var key_pressed;
  if(event == null){
    key_pressed = window.event.keyCode;
  } else {
    key_pressed = event.keyCode;
  }
  for (var i=0;i<players.length;i++) {
    for (key in players[i].keys) {
      if (key_pressed == players[i].keys[key].code){
        players[i].keys[key].hold=true;
      }
    }
  }
}
 
document.onkeyup = function(event) {
  var key_pressed;
  if(event == null){
    key_pressed = window.event.keyCode;
  } else {
    key_pressed = event.keyCode;
  }
  for (var i=0;i<players.length;i++) {
    for (key in players[i].keys) {
      if (key_pressed == players[i].keys[key].code){
        players[i].keys[key].hold=false;
      }
    }
  }
}

function on_enter_frame(){
  if(collisionCircleBox(blue.avatar,goal)){
    onWin();
    context.fillStyle=blue.avatar.color;
    context.textAlign="center";
    context.font="200px Arial";
    context.fillText("Gagné!",width/2,height/2);
  } else {
    var collisionCheck=false;
    for (var i=0;i<players.length;i++) {
      players[i].updateFriction();
      players[i].updateCommands();
      collisionCheck|=players[i].updateCollisionBorder();
    }

    collisionCheck|=players[1].updateCollisionSameMass(players[0]);
    collisionCheck|=players[1].updateCollisionInfiniteMass(players[2]);
 
    if(collisionCheck){onCollision();}

    context.clearRect(0,0,width,height);
    context.fillStyle=blue.avatar.color;
    context.fillRect(goal.x,goal.y,goal.width,goal.height);
 
    for (var i=players.length-1;i>-1;i--) {
      players[i].updatePosition();
      players[i].draw();
    }
  }
}
