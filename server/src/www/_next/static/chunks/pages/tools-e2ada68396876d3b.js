(self.webpackChunk_N_E=self.webpackChunk_N_E||[]).push([[289],{6221:function(e,t,n){"use strict";n.r(t);var r=n(29),o=n(9499),i=n(7794),s=n.n(i),c=n(480),a=n(7294),l=n(313),u=n(2043),d=n(8790),p=n(5419),f=n(5193),h=n(8609),x=n(4612),v=n(7398),j=n(7375),m=n(6886),g=n(2821),b=n(5434),w=n(3854),C=n(1163),y=n(5893);function S(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function k(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?S(Object(n),!0).forEach((function(t){(0,o.Z)(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):S(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}var z=(0,a.createContext)();function _(e){var t=e.tools,n=void 0===t?[]:t,r=e.setCurrentTool,o=e.currentTool,i=void 0===o?0:o,s=(0,l.Fg)(),c=s.bg,a=s.color,u=s.bgHover;return(0,y.jsx)(y.Fragment,{children:(0,y.jsxs)(p.v2,{children:[(0,y.jsx)(p.j2,{alignItems:"center",bgColor:c,size:"lg",color:a,_hover:{bgColor:u},_expanded:{bgColor:u},_focus:{boxShadow:"outline"},as:f.zx,rightIcon:(0,y.jsx)(g.Duv,{size:"1.4em"}),children:n[i]?n[i].label:"Tools"}),(0,y.jsx)(p.qy,{children:n.map((function(e,t){return(0,y.jsx)(p.sN,{onClick:function(){return r(t)},children:e.label},t)}))})]})})}function D(){var e=(0,h.pm)(),t=(0,a.useState)(!1),n=t[0],o=t[1],i=(0,a.useContext)(z),l=i.downloadDocument,u=i.stateSetter,p=l.publicCode;function v(){return j.apply(this,arguments)}function j(){return(j=(0,r.Z)(s().mark((function t(){var n;return s().wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return o(!0),t.prev=1,t.next=4,(0,c.W)("post","dac")({public_code:p});case 4:if("failed"!==(null===(n=t.sent)||void 0===n?void 0:n.data.status)){t.next=7;break}throw n.data.msg;case 7:if("error"!==(null===n||void 0===n?void 0:n.data)){t.next=9;break}throw"Something wrong, Please contact with support to solve this problem.";case 9:setResult(data.data),onOpen(),t.next=16;break;case 13:t.prev=13,t.t0=t.catch(1),e({title:"Search failed.",description:t.t0.toString(),status:"error",position:"top",duration:9e3,isClosable:!0});case 16:o(!1);case 17:case"end":return t.stop()}}),t,null,[[1,13]])})))).apply(this,arguments)}return(0,y.jsx)(y.Fragment,{children:(0,y.jsxs)(d.Kq,{w:"full",spacing:5,align:"center",children:[(0,y.jsxs)(d.Kq,{children:[(0,y.jsx)(d.X6,{size:"xs",children:"Public Code"}),(0,y.jsx)(x.II,{value:p,onChange:function(e){return u("downloadDocument.publicCode",e.target.value)},size:"md",variant:"filled",placeholder:"Code"})]}),(0,y.jsx)(f.zx,{isDisabled:!p,isLoading:n,onClick:(0,r.Z)(s().mark((function e(){return s().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,v();case 2:case"end":return e.stop()}}),e)}))),maxW:"min-content",rightIcon:(0,y.jsx)(g.RFS,{size:"1.4em"}),children:"Download"})]})})}function O(){var e,t=(0,h.pm)(),n=(0,a.useState)(!1),i=n[0],l=n[1],p=(0,a.useState)(),j=(p[0],p[1]),m=(0,a.useContext)(z),g=m.verifyDocument,C=m.stateSetter,S=(0,a.useState)(""),k=S[0],_=S[1],D=g.verifyCode,O=g.file;function I(){return P.apply(this,arguments)}function P(){return(P=(0,r.Z)(s().mark((function e(){var n;return s().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return l(!0),e.prev=1,e.next=4,(0,c.W)("post","vad")(k,{"Content-Type":"application/pdf",verify_code:D});case 4:if("failed"!==(null===(n=e.sent)||void 0===n?void 0:n.data.status)){e.next=7;break}throw n.data.msg;case 7:if("error"!==(null===n||void 0===n?void 0:n.data)){e.next=9;break}throw"Something wrong, Please try again or contact with support to solve this problem.";case 9:console.log(n.data),j(n.data),e.next=16;break;case 13:e.prev=13,e.t0=e.catch(1),t({title:"Verify failed.",description:e.t0.toString(),status:"error",position:"top",duration:9e3,isClosable:!0});case 16:l(!1);case 17:case"end":return e.stop()}}),e,null,[[1,13]])})))).apply(this,arguments)}return(0,y.jsxs)(d.Kq,{w:"full",spacing:8,align:"center",children:[(0,y.jsxs)(d.Kq,{children:[(0,y.jsx)(d.X6,{size:"xs",children:"Verification Code"}),(0,y.jsx)(x.II,{size:"md",variant:"filled",placeholder:"Code",value:D,onChange:function(e){C("verifyDocument.verifyCode",e.target.value)}}),(0,y.jsxs)(d.Ug,{justify:"end",children:[O&&(0,y.jsx)(u.UL,{file:O}),(0,y.jsx)(v.u,{hasArrow:!0,placement:"top",label:"Import the document which you need to verify",children:(0,y.jsxs)(d.xu,{position:"relative",w:"max-content",children:[(0,y.jsx)(f.zx,{size:"sm",rightIcon:(0,y.jsx)(w.ys2,{size:"1.4em"}),children:"Import File"}),(0,y.jsx)(x.II,(e={w:"full",type:"file",accept:".pdf",left:0,onChange:function(){var e=(0,r.Z)(s().mark((function e(t){var n;return s().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:if("undefined"===typeof(n=t.target.files[0])){e.next=8;break}return C("verifyDocument.file",n),e.t0=_,e.next=6,null===n||void 0===n?void 0:n.text();case 6:e.t1=e.sent,(0,e.t0)(e.t1);case 8:case"end":return e.stop()}}),e)})));return function(t){return e.apply(this,arguments)}}(),top:0,cursor:"pointer",position:"absolute",opacity:0},(0,o.Z)(e,"w","full"),(0,o.Z)(e,"h","full"),e))]})})]})]}),(0,y.jsx)(f.zx,{onClick:(0,r.Z)(s().mark((function e(){return s().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,I();case 2:case"end":return e.stop()}}),e)}))),isDisabled:!O||!D,isLoading:i,maxW:"min-content",rightIcon:(0,y.jsx)(b.Ac1,{size:"1.4em"}),children:"Verify"})]})}function I(){var e,t,n,o,i=(0,h.pm)(),l=(0,C.useRouter)(),p=(0,a.useState)({}),v=p[0],b=p[1],w=(0,a.useState)(!1),S=w[0],_=w[1],D=(0,j.qY)(),O=D.onOpen,I=D.onClose,P=(0,a.useContext)(z),q=P.searchDocument,K=P.stateSetter,Z=q.publicCode;function N(){return X.apply(this,arguments)}function X(){return(X=(0,r.Z)(s().mark((function e(){var t,n,r;return s().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return _(!0),e.prev=1,e.next=4,(0,c.W)("post","sfad")({public_code:Z});case 4:if("failed"!==(null===(r=e.sent)||void 0===r||null===(t=r.data)||void 0===t?void 0:t.status)){e.next=7;break}throw null===r||void 0===r||null===(n=r.data)||void 0===n?void 0:n.msg;case 7:b(null===r||void 0===r?void 0:r.data),O(),e.next=14;break;case 11:e.prev=11,e.t0=e.catch(1),i({title:"Search failed.",description:e.t0.toString(),status:"error",position:"top",duration:9e3,isClosable:!0});case 14:_(!1);case 15:case"end":return e.stop()}}),e,null,[[1,11]])})))).apply(this,arguments)}return(0,y.jsxs)(y.Fragment,{children:[(0,y.jsxs)(d.Kq,{w:"full",spacing:5,align:"center",children:[(0,y.jsxs)(d.Kq,{children:[(0,y.jsx)(d.X6,{size:"xs",children:"Public Code"}),(0,y.jsx)(x.II,{value:Z,onChange:function(e){return K("searchDocument.publicCode",e.target.value)},size:"md",variant:"filled",placeholder:"Code"})]}),(0,y.jsx)(f.zx,{isDisabled:!Z,isLoading:S,onClick:(0,r.Z)(s().mark((function e(){return s().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,N();case 2:case"end":return e.stop()}}),e)}))),maxW:"min-content",rightIcon:(0,y.jsx)(g.RB5,{size:"1.4em"}),children:"Search"})]}),(0,y.jsx)(u.u_,k(k({},D),{},{header:"Search result ",footer:(0,y.jsx)(f.zx,{onClick:I,children:"Close"}),children:(0,y.jsxs)(d.Kq,{children:[["document_name","verifier","writer","date_of_publication"].filter((function(e){return!v.has})).length>0&&(0,y.jsxs)(m.bZ,{status:"error",children:[(0,y.jsx)(m.zM,{}),(0,y.jsxs)("div",{children:["Some data isn't found, Please"," ",(0,y.jsx)("span",{style:{textDecoration:"underline",cursor:"pointer"},onClick:function(){return l.push("/support")},children:"contact us"})," ","to solve this problem."]})]}),(0,y.jsx)(d.iz,{}),(0,y.jsxs)(d.Kq,{spacing:2,children:[(0,y.jsxs)(d.Kq,{spacing:0,children:[(0,y.jsx)(d.X6,{fontSize:12,children:"Document name"}),(0,y.jsx)(d.xv,{children:null!==(e=null===v||void 0===v?void 0:v.document_name)&&void 0!==e?e:"Not found"})]}),(0,y.jsxs)(d.Kq,{spacing:0,children:[(0,y.jsx)(d.X6,{fontSize:12,children:"Verifier"}),(0,y.jsx)(d.xv,{children:null!==(t=null===v||void 0===v?void 0:v.verifier)&&void 0!==t?t:"Not found"})]}),(0,y.jsxs)(d.Kq,{spacing:0,children:[(0,y.jsx)(d.X6,{fontSize:12,children:"Writer"}),(0,y.jsx)(d.xv,{children:null!==(n=null===v||void 0===v?void 0:v.writer)&&void 0!==n?n:"Not found"})]}),(0,y.jsxs)(d.Kq,{spacing:0,children:[(0,y.jsx)(d.X6,{fontSize:12,children:"Date of publication"}),(0,y.jsx)(d.xv,{children:null!==(o=null===v||void 0===v?void 0:v.date_of_publication)&&void 0!==o?o:"Not found"})]})]})]})}))]})}t.default=function(){var e,t=(0,a.useState)({downloadDocument:{publicCode:""},verifyDocument:{file:null,verifyCode:""},searchDocument:{publicCode:""}}),n=t[0],r=t[1],i=[{label:"Download a document",component:(0,y.jsx)(D,{})},{label:"Verify a document",component:(0,y.jsx)(O,{})},{label:"Search for a document",component:(0,y.jsx)(I,{})}],s=(0,a.useState)(0),c=s[0],u=s[1],p=(0,l.Fg)();return p.bg,p.color,p.bgHover,(0,y.jsx)(z.Provider,{value:k(k({},n),{},{stateSetter:function(e,t){var i=e.split(".").map((function(e){return e.trim()}));return function(e){r(k(k({},n),{},(0,o.Z)({},i[0],k(k({},n[i[0]]),{},(0,o.Z)({},i[1],e)))))}}}),children:(0,y.jsxs)(d.Kq,{align:"center",spacing:10,justify:"center",h:"full",children:[(0,y.jsx)(_,{tools:i,currentTool:c,setCurrentTool:u}),i[c]&&(null===(e=i[c])||void 0===e?void 0:e.component)]})})}},4565:function(e,t,n){(window.__NEXT_P=window.__NEXT_P||[]).push(["/tools",function(){return n(6221)}])}},function(e){e.O(0,[612,202,774,888,179],(function(){return t=4565,e(e.s=t);var t}));var t=e.O();_N_E=t}]);