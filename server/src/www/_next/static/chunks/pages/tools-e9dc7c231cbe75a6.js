(self.webpackChunk_N_E=self.webpackChunk_N_E||[]).push([[289],{6475:function(e,t,n){"use strict";n.d(t,{O:function(){return l}});var r=n(9499),o=n(2640);function i(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function s(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?i(Object(n),!0).forEach((function(t){(0,r.Z)(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}var c=n(7294),a=(c.useContext,c.useState);function l(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:null,t=a(e),n=(0,o.Z)(t,2),i=n[0],c=n[1];function l(e,t){var n=e.split(".").map((function(e){return e.trim()}));return function(e){c(s(s({},i),{},(0,r.Z)({},n[0],s(s({},i[n[0]]),{},(0,r.Z)({},n[1],e)))))}(t)}function u(){c(e)}return[i,l,u]}},6221:function(e,t,n){"use strict";n.r(t);var r=n(29),o=n(9499),i=n(2640),s=n(7794),c=n.n(s),a=n(4626),l=n(6415),u=n(7294),d=n(313),f=n(6475),p=n(3150),h=n(8527),v=n(5419),x=n(5193),j=n(8609),m=n(4612),b=n(7398),g=n(7375),w=n(6886),y=n(2821),O=n(5434),C=n(3854),k=n(1163),S=n(5893);function D(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function z(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?D(Object(n),!0).forEach((function(t){(0,o.Z)(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):D(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}var P=(0,u.createContext)();function _(e){var t=e.tools,n=void 0===t?[]:t,r=e.setCurrentTool,o=e.currentTool,i=void 0===o?0:o,s=(0,d.Fg)(),c=s.bg,a=s.color,l=s.bgHover;return(0,S.jsx)(S.Fragment,{children:(0,S.jsxs)(v.v2,{children:[(0,S.jsx)(v.j2,{alignItems:"center",bgColor:c,size:"lg",color:a,_hover:{bgColor:l},_expanded:{bgColor:l},_focus:{boxShadow:"outline"},as:x.zx,rightIcon:(0,S.jsx)(y.Duv,{size:"1.4em"}),children:n[i]?n[i].label:"Tools"}),(0,S.jsx)(v.qy,{children:n.map((function(e,t){return(0,S.jsx)(v.sN,{onClick:function(){return r(t)},children:e.label},t)}))})]})})}function I(){var e=(0,j.pm)(),t=(0,u.useState)(!1),n=t[0],o=t[1],i=(0,u.useContext)(P),s=i.downloadDocument,d=i.stateSetter,f=i.btnRef,p=s.code;function v(){return b.apply(this,arguments)}function b(){return(b=(0,r.Z)(c().mark((function t(){var n,r;return c().wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return o(!0),t.prev=1,t.next=4,(0,a.W)("post",l.K0.DOWNLOAD)({code:p},null,{responseType:"blob"});case 4:if("failed"!==(null===(n=t.sent)||void 0===n?void 0:n.data.status)){t.next=7;break}throw n.data.msg;case 7:if("error"!==(null===n||void 0===n?void 0:n.data)){t.next=9;break}throw"Something wrong, Please contact with support to solve this problem.";case 9:(r=document.createElement("a")).target="_blank",r.download="".concat(p,".").concat(n.headers.extension),r.href=URL.createObjectURL(n.data),document.body.appendChild(r),r.click(),r.parentNode.removeChild(r),t.next=21;break;case 18:t.prev=18,t.t0=t.catch(1),e({title:"Download failed.",description:t.t0.toString(),status:"error",position:"top",duration:9e3,isClosable:!0});case 21:o(!1);case 22:case"end":return t.stop()}}),t,null,[[1,18]])})))).apply(this,arguments)}return(0,S.jsx)(S.Fragment,{children:(0,S.jsxs)(h.Kq,{w:"full",spacing:5,align:"center",children:[(0,S.jsxs)(h.Kq,{children:[(0,S.jsx)(h.X6,{size:"xs",children:"Code"}),(0,S.jsx)(m.II,{value:p,onChange:function(e){return d("downloadDocument.code",e.target.value)},size:"md",variant:"filled",placeholder:"Code"})]}),(0,S.jsx)(x.zx,{ref:f,isDisabled:!p,isLoading:n,onClick:(0,r.Z)(c().mark((function e(){return c().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,v();case 2:case"end":return e.stop()}}),e)}))),maxW:"min-content",rightIcon:(0,S.jsx)(y.RFS,{size:"1.4em"}),children:"Download"})]})})}function E(){var e,t=(0,j.pm)(),n=(0,u.useState)(!1),i=n[0],s=n[1],d=(0,u.useState)(),f=(d[0],d[1]),v=(0,u.useContext)(P),g=v.verifyDocument,w=v.stateSetter,y=v.btnRef,k=g.code,D=g.file;function z(){return _.apply(this,arguments)}function _(){return(_=(0,r.Z)(c().mark((function e(){var n,r;return c().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return s(!0),e.prev=1,e.next=4,D.arrayBuffer();case 4:return n=e.sent,e.next=7,(0,a.W)("post",l.K0.VERIFY)(new Uint8Array(n),{"Content-Type":"application/pdf",code:k});case 7:if("failed"!==(null===(r=e.sent)||void 0===r?void 0:r.data.status)){e.next=10;break}throw r.data.msg;case 10:if("error"!==(null===r||void 0===r?void 0:r.data)){e.next=12;break}throw"Something wrong, Please try again or contact with support to solve this problem.";case 12:f(r.data),t({title:"Result.",description:r.data.msg,status:"info",position:"top",duration:9e3,isClosable:!0}),e.next=19;break;case 16:e.prev=16,e.t0=e.catch(1),t({title:"Verify failed.",description:e.t0.toString(),status:"error",position:"top",duration:9e3,isClosable:!0});case 19:s(!1);case 20:case"end":return e.stop()}}),e,null,[[1,16]])})))).apply(this,arguments)}return(0,S.jsxs)(h.Kq,{w:"full",spacing:8,align:"center",children:[(0,S.jsxs)(h.Kq,{children:[(0,S.jsx)(h.X6,{size:"xs",children:"Code"}),(0,S.jsx)(m.II,{size:"md",variant:"filled",placeholder:"Code",value:k,onChange:function(e){w("verifyDocument.code",e.target.value)}}),(0,S.jsxs)(h.Ug,{justify:"end",children:[D&&(0,S.jsx)(p.UL,{file:D}),(0,S.jsx)(b.u,{hasArrow:!0,placement:"top",label:"Import the document which you need to verify",children:(0,S.jsxs)(h.xu,{position:"relative",w:"max-content",children:[(0,S.jsx)(x.zx,{size:"sm",rightIcon:(0,S.jsx)(C.ys2,{size:"1.4em"}),children:"Import File"}),(0,S.jsx)(m.II,(e={w:"full",type:"file",accept:".pdf",left:0,onChange:function(){var e=(0,r.Z)(c().mark((function e(t){var n;return c().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:"undefined"!==typeof(n=t.target.files[0])&&w("verifyDocument.file",n);case 2:case"end":return e.stop()}}),e)})));return function(t){return e.apply(this,arguments)}}(),top:0,cursor:"pointer",position:"absolute",opacity:0},(0,o.Z)(e,"w","full"),(0,o.Z)(e,"h","full"),e))]})})]})]}),(0,S.jsx)(x.zx,{ref:y,onClick:(0,r.Z)(c().mark((function e(){return c().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,z();case 2:case"end":return e.stop()}}),e)}))),isDisabled:!D||!k,isLoading:i,maxW:"min-content",rightIcon:(0,S.jsx)(O.Ac1,{size:"1.4em"}),children:"Verify"})]})}function K(){var e,t,n,o,i=(0,j.pm)(),s=(0,k.useRouter)(),d=(0,u.useState)({}),f=d[0],v=d[1],b=(0,u.useState)(!1),O=b[0],C=b[1],D=(0,g.qY)(),_=D.onOpen,I=D.onClose,E=(0,u.useContext)(P),K=E.searchDocument,Z=E.stateSetter,q=E.btnRef,R=K.code;function N(){return L.apply(this,arguments)}function L(){return(L=(0,r.Z)(c().mark((function e(){var t,n,r;return c().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return C(!0),e.prev=1,e.next=4,(0,a.W)("post",l.K0.SEARCH)({code:R});case 4:if("failed"!==(null===(r=e.sent)||void 0===r||null===(t=r.data)||void 0===t?void 0:t.status)){e.next=7;break}throw null===r||void 0===r||null===(n=r.data)||void 0===n?void 0:n.msg;case 7:v(null===r||void 0===r?void 0:r.data),_(),e.next=14;break;case 11:e.prev=11,e.t0=e.catch(1),i({title:"Search failed.",description:e.t0.toString(),status:"error",position:"top",duration:9e3,isClosable:!0});case 14:C(!1);case 15:case"end":return e.stop()}}),e,null,[[1,11]])})))).apply(this,arguments)}return(0,S.jsxs)(S.Fragment,{children:[(0,S.jsxs)(h.Kq,{w:"full",spacing:5,align:"center",children:[(0,S.jsxs)(h.Kq,{children:[(0,S.jsx)(h.X6,{size:"xs",children:"Code"}),(0,S.jsx)(m.II,{value:R,onChange:function(e){return Z("searchDocument.code",e.target.value)},size:"md",variant:"filled",placeholder:"Code"})]}),(0,S.jsx)(x.zx,{ref:q,isDisabled:!R,isLoading:O,onClick:(0,r.Z)(c().mark((function e(){return c().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,N();case 2:case"end":return e.stop()}}),e)}))),maxW:"min-content",rightIcon:(0,S.jsx)(y.RB5,{size:"1.4em"}),children:"Search"})]}),(0,S.jsx)(p.u_,z(z({},D),{},{header:"Search result ",footer:(0,S.jsx)(x.zx,{onClick:I,children:"Close"}),children:(0,S.jsxs)(h.Kq,{children:[["document_name","verifier","writer","date_of_publication"].filter((function(e){return!f.hasOwnProperty(e)})).length>0&&(0,S.jsxs)(w.bZ,{status:"error",children:[(0,S.jsx)(w.zM,{}),(0,S.jsxs)("div",{children:["Some data isn't found, Please"," ",(0,S.jsx)("span",{style:{textDecoration:"underline",cursor:"pointer"},onClick:function(){return s.push("/support")},children:"contact us"})," ","to solve this problem."]})]}),(0,S.jsx)(h.iz,{}),(0,S.jsxs)(h.Kq,{spacing:2,children:[(0,S.jsxs)(h.Kq,{spacing:0,children:[(0,S.jsx)(h.X6,{fontSize:12,children:"Document name"}),(0,S.jsx)(h.xv,{children:null!==(e=null===f||void 0===f?void 0:f.document_name)&&void 0!==e?e:"Not found"})]}),(0,S.jsxs)(h.Kq,{spacing:0,children:[(0,S.jsx)(h.X6,{fontSize:12,children:"Verifier"}),(0,S.jsx)(h.xv,{children:null!==(t=null===f||void 0===f?void 0:f.verifier)&&void 0!==t?t:"Not found"})]}),(0,S.jsxs)(h.Kq,{spacing:0,children:[(0,S.jsx)(h.X6,{fontSize:12,children:"Writer"}),(0,S.jsx)(h.xv,{children:null!==(n=null===f||void 0===f?void 0:f.writer)&&void 0!==n?n:"Not found"})]}),(0,S.jsxs)(h.Kq,{spacing:0,children:[(0,S.jsx)(h.X6,{fontSize:12,children:"Date of publication"}),(0,S.jsx)(h.xv,{children:null!==(o=null===f||void 0===f?void 0:f.date_of_publication)&&void 0!==o?o:"Not found"})]})]})]})}))]})}t.default=function(){var e,t=(0,f.O)({downloadDocument:{code:""},verifyDocument:{file:null,code:""},searchDocument:{code:""}}),n=(0,i.Z)(t,2),r=n[0],o=n[1],s=[{label:"Download a document",component:(0,S.jsx)(I,{})},{label:"Verify a document",component:(0,S.jsx)(E,{})},{label:"Search for a document",component:(0,S.jsx)(K,{})}],c=(0,u.useState)(0),a=c[0],l=c[1],p=(0,u.useRef)(null),v=(0,d.Fg)();return v.bg,v.color,v.bgHover,(0,u.useEffect)((function(){var e=function(e){if("Enter"===e.key)p.current.click()};return window.addEventListener("keydown",e),function(){return window.removeEventListener("keydown",e)}}),[]),(0,S.jsx)(P.Provider,{value:z(z({},r),{},{stateSetter:o,btnRef:p}),children:(0,S.jsxs)(h.Kq,{align:"center",spacing:10,justify:"center",h:"full",children:[(0,S.jsx)(_,{tools:s,currentTool:a,setCurrentTool:l}),s[a]&&(null===(e=s[a])||void 0===e?void 0:e.component)]})})}},4565:function(e,t,n){(window.__NEXT_P=window.__NEXT_P||[]).push(["/tools",function(){return n(6221)}])}},function(e){e.O(0,[612,980,774,888,179],(function(){return t=4565,e(e.s=t);var t}));var t=e.O();_N_E=t}]);