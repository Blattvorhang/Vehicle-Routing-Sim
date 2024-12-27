(function(){var t={8396:function(t,n,e){"use strict";var o=e(5130),a=e(6768);const i={id:"app"};function r(t,n,e,o,r,s){const l=(0,a.g2)("Map");return(0,a.uX)(),(0,a.CE)("div",i,[(0,a.bF)(l)])}var s=e(4232);const l={class:"layout-container"},c={class:"content"},u={key:0,class:"map-container"},p={class:"map"},d=["src","alt"],f={key:1},g={class:"passenger-info"},y={class:"passenger-table"};function h(t,n,e,o,i,r){return(0,a.uX)(),(0,a.CE)("div",l,[n[2]||(n[2]=(0,a.Lk)("h1",null,"Map Viewer",-1)),(0,a.Lk)("div",c,[i.grid.length?((0,a.uX)(),(0,a.CE)("div",u,[(0,a.Lk)("div",p,[((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(i.grid,((t,n)=>((0,a.uX)(),(0,a.CE)("div",{key:n,class:"row"},[((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(t,((t,n)=>((0,a.uX)(),(0,a.CE)("div",{key:n,class:(0,s.C4)(["cell",t?"black":"whiteblack"])},null,2)))),128))])))),128)),((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(i.cars,((t,n)=>((0,a.uX)(),(0,a.CE)("img",{key:n,src:t.image,alt:`Car ${n+1}`,class:"car",style:(0,s.Tr)({top:t.position.y*i.cellSize+"px",left:t.position.x*i.cellSize+"px",transform:`rotate(${t.angle}deg)`})},null,12,d)))),128)),((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(i.passengers,((t,n)=>((0,a.uX)(),(0,a.CE)("div",{key:n,class:"passenger",style:(0,s.Tr)({top:t.position.y*i.cellSize+"px",left:t.position.x*i.cellSize+"px",backgroundColor:"WAITING"===t.status?"red":"blue"})},[(0,a.Lk)("span",null,(0,s.v_)(n),1)],4)))),128)),((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(i.passengers,((t,n)=>((0,a.uX)(),(0,a.CE)("div",{key:"dest-"+n,class:"destination",style:(0,s.Tr)({top:t.destination.y*i.cellSize+"px",left:t.destination.x*i.cellSize+"px"})}," D"+(0,s.v_)(n),5)))),128))])])):((0,a.uX)(),(0,a.CE)("p",f,"Loading map data...")),(0,a.Lk)("div",g,[n[1]||(n[1]=(0,a.Lk)("h2",null,"Passenger Information",-1)),(0,a.Lk)("table",y,[n[0]||(n[0]=(0,a.Lk)("thead",null,[(0,a.Lk)("tr",null,[(0,a.Lk)("th",null,"Index"),(0,a.Lk)("th",null,"Start Position"),(0,a.Lk)("th",null,"Destination"),(0,a.Lk)("th",null,"Status"),(0,a.Lk)("th",null,"Target Car")])],-1)),(0,a.Lk)("tbody",null,[((0,a.uX)(!0),(0,a.CE)(a.FK,null,(0,a.pI)(i.passengers,((t,n)=>((0,a.uX)(),(0,a.CE)("tr",{key:n},[(0,a.Lk)("td",null,(0,s.v_)(t.index),1),(0,a.Lk)("td",null,(0,s.v_)(t.position.x)+", "+(0,s.v_)(t.position.y),1),(0,a.Lk)("td",null,(0,s.v_)(t.destination.x)+", "+(0,s.v_)(t.destination.y),1),(0,a.Lk)("td",null,(0,s.v_)("WAITING"===t.status?"Waiting":`Picked up by Car ${t.nowCarIndex}`),1),(0,a.Lk)("td",null,(0,s.v_)(void 0!==t.nowCarIndex?t.nowCarIndex:"N/A"),1)])))),128))])])])])])}e(1454);var v=e(4373);const x="http://localhost:8888",k=async()=>{try{const t=await v.A.get(`${x}/api/init`);return t.data}catch(t){throw console.error("Failed to fetch initial positions:",t),t}},m=async()=>{try{const t=await v.A.get(`${x}/api/map/select`);return t.data}catch(t){throw console.error("Failed to fetch map data:",t),t}},b=async()=>{try{const t=await v.A.get(`${x}/api/car/select/all`);return t.data}catch(t){throw console.error("Failed to fetch car and passenger info:",t),t}};var C={name:"MapWithCars",data(){return{grid:[],cars:[{image:e(6635),position:{x:0,y:0},angle:0},{image:e(772),position:{x:0,y:0},angle:0},{image:e(3259),position:{x:0,y:0},angle:0},{image:e(8625),position:{x:0,y:0},angle:0},{image:e(7928),position:{x:0,y:0},angle:0}],passengers:[],cellSize:1}},methods:{async loadMapAndPositions(){try{const t=await m();this.grid=t;const n=await k(),{cars:e,passengers:o}=n;this.cars=e.map(((t,n)=>({...this.cars[n],position:{x:t.carX,y:t.carY},angle:t.carAngle}))),this.passengers=o.map((t=>({index:t.passengerIndex,position:{x:t.startX,y:t.startY},destination:{x:t.endX,y:t.endY},status:t.status,nowCarIndex:t.nowCarIndex}))),console.log("Map and positions loaded successfully.")}catch(t){console.error("Failed to load map and positions:",t),alert("Failed to load map or position data!")}},async updateCarPositions(){try{const t=await b(),n=t;this.cars=this.cars.map(((t,e)=>({...t,position:{x:n[e].carX,y:n[e].carY},angle:n[e].carAngle}))),console.log("Update car position successfully.")}catch(t){console.error("Failed to update car positions:",t)}}},mounted(){this.loadMapAndPositions(),setInterval(this.updateCarPositions,1e3)}},w=e(1241);const L=(0,w.A)(C,[["render",h]]);var X=L,I={name:"App",components:{Map:X}};const E=(0,w.A)(I,[["render",r]]);var F=E,S=e(5129),A=e.n(S);const O=(0,o.Ef)(F);O.use(A()),O.mount("#app")},5129:function(){},7928:function(t,n,e){"use strict";t.exports=e.p+"img/blue.0ba85057.png"},8625:function(t,n,e){"use strict";t.exports=e.p+"img/gray.958efaed.png"},3259:function(t,n,e){"use strict";t.exports=e.p+"img/green.50e50b03.png"},6635:function(t,n,e){"use strict";t.exports=e.p+"img/red.de238d59.png"},772:function(t,n,e){"use strict";t.exports=e.p+"img/yellow.2402b849.png"}},n={};function e(o){var a=n[o];if(void 0!==a)return a.exports;var i=n[o]={exports:{}};return t[o].call(i.exports,i,i.exports,e),i.exports}e.m=t,function(){var t=[];e.O=function(n,o,a,i){if(!o){var r=1/0;for(u=0;u<t.length;u++){o=t[u][0],a=t[u][1],i=t[u][2];for(var s=!0,l=0;l<o.length;l++)(!1&i||r>=i)&&Object.keys(e.O).every((function(t){return e.O[t](o[l])}))?o.splice(l--,1):(s=!1,i<r&&(r=i));if(s){t.splice(u--,1);var c=a();void 0!==c&&(n=c)}}return n}i=i||0;for(var u=t.length;u>0&&t[u-1][2]>i;u--)t[u]=t[u-1];t[u]=[o,a,i]}}(),function(){e.n=function(t){var n=t&&t.__esModule?function(){return t["default"]}:function(){return t};return e.d(n,{a:n}),n}}(),function(){e.d=function(t,n){for(var o in n)e.o(n,o)&&!e.o(t,o)&&Object.defineProperty(t,o,{enumerable:!0,get:n[o]})}}(),function(){e.g=function(){if("object"===typeof globalThis)return globalThis;try{return this||new Function("return this")()}catch(t){if("object"===typeof window)return window}}()}(),function(){e.o=function(t,n){return Object.prototype.hasOwnProperty.call(t,n)}}(),function(){e.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})}}(),function(){e.p="/"}(),function(){var t={524:0};e.O.j=function(n){return 0===t[n]};var n=function(n,o){var a,i,r=o[0],s=o[1],l=o[2],c=0;if(r.some((function(n){return 0!==t[n]}))){for(a in s)e.o(s,a)&&(e.m[a]=s[a]);if(l)var u=l(e)}for(n&&n(o);c<r.length;c++)i=r[c],e.o(t,i)&&t[i]&&t[i][0](),t[i]=0;return e.O(u)},o=self["webpackChunkhello"]=self["webpackChunkhello"]||[];o.forEach(n.bind(null,0)),o.push=n.bind(null,o.push.bind(o))}();var o=e.O(void 0,[504],(function(){return e(8396)}));o=e.O(o)})();
//# sourceMappingURL=app.c4fb114e.js.map