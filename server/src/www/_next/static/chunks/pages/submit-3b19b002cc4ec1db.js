(self.webpackChunk_N_E=self.webpackChunk_N_E||[]).push([[453],{7398:function(e,t,n){"use strict";n.d(t,{u:function(){return j}});var r=n(1662),o=n(6871),i=n(1604),s=n(9703),a=n(1358),l=n(3139),u=n(1190),c=n(7294),d=n(7375),p=n(8698),f=n(6450);function h(){return h=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var n=arguments[t];for(var r in n)Object.prototype.hasOwnProperty.call(n,r)&&(e[r]=n[r])}return e},h.apply(this,arguments)}function v(e,t){if(null==e)return{};var n,r,o={},i=Object.keys(e);for(r=0;r<i.length;r++)n=i[r],t.indexOf(n)>=0||(o[n]=e[n]);return o}var m={exit:{scale:.85,opacity:0,transition:{opacity:{duration:.15,easings:"easeInOut"},scale:{duration:.2,easings:"easeInOut"}}},enter:{scale:1,opacity:1,transition:{opacity:{easings:"easeOut",duration:.2},scale:{duration:.2,ease:[.175,.885,.4,1.1]}}}},g=["openDelay","closeDelay","closeOnClick","closeOnMouseDown","onOpen","onClose","placement","id","isOpen","defaultIsOpen","arrowSize","arrowShadowColor","arrowPadding","modifiers","isDisabled","gutter","offset","direction"];var x=["children","label","shouldWrapChildren","aria-label","hasArrow","bg","portalProps","background","backgroundColor","bgColor"],b=(0,i.m$)(l.E.div),j=(0,i.Gp)((function(e,t){var n,l,j=(0,i.mq)("Tooltip",e),w=(0,i.Lr)(e),y=(0,i.Fg)(),O=w.children,D=w.label,C=w.shouldWrapChildren,k=w["aria-label"],P=w.hasArrow,S=w.bg,E=w.portalProps,_=w.background,z=w.backgroundColor,I=w.bgColor,T=v(w,x),R=null!=(n=null!=(l=null!=_?_:z)?l:S)?n:I;R&&(j.bg=R,j[r.j.arrowBg.var]=(0,s.K1)(y,"colors",R));var A,q=function(e){void 0===e&&(e={});var t=e,n=t.openDelay,o=void 0===n?0:n,i=t.closeDelay,a=void 0===i?0:i,l=t.closeOnClick,u=void 0===l||l,m=t.closeOnMouseDown,x=t.onOpen,b=t.onClose,j=t.placement,w=t.id,y=t.isOpen,O=t.defaultIsOpen,D=t.arrowSize,C=void 0===D?10:D,k=t.arrowShadowColor,P=t.arrowPadding,S=t.modifiers,E=t.isDisabled,_=t.gutter,z=t.offset,I=t.direction,T=v(t,g),R=(0,d.qY)({isOpen:y,defaultIsOpen:O,onOpen:x,onClose:b}),A=R.isOpen,q=R.onOpen,N=R.onClose,K=(0,r.D)({enabled:A,placement:j,arrowPadding:P,modifiers:S,gutter:_,offset:z,direction:I}),Z=K.referenceRef,U=K.getPopperProps,M=K.getArrowInnerProps,X=K.getArrowProps,B=(0,d.Me)(w,"tooltip"),F=c.useRef(null),W=c.useRef(),$=c.useRef(),G=c.useCallback((function(){E||(W.current=window.setTimeout(q,o))}),[E,q,o]),L=c.useCallback((function(){W.current&&clearTimeout(W.current),$.current=window.setTimeout(N,a)}),[a,N]),Y=c.useCallback((function(){u&&L()}),[u,L]),H=c.useCallback((function(){m&&L()}),[m,L]),J=c.useCallback((function(e){A&&"Escape"===e.key&&L()}),[A,L]);(0,p.b)("keydown",J),c.useEffect((function(){return function(){clearTimeout(W.current),clearTimeout($.current)}}),[]),(0,p.b)("mouseleave",L,(function(){return F.current}));var Q=c.useCallback((function(e,t){return void 0===e&&(e={}),void 0===t&&(t=null),h({},e,{ref:(0,f.lq)(F,t,Z),onMouseEnter:(0,s.v0)(e.onMouseEnter,G),onClick:(0,s.v0)(e.onClick,Y),onMouseDown:(0,s.v0)(e.onMouseDown,H),onFocus:(0,s.v0)(e.onFocus,G),onBlur:(0,s.v0)(e.onBlur,L),"aria-describedby":A?B:void 0})}),[G,L,H,A,B,Y,Z]),V=c.useCallback((function(e,t){var n;return void 0===e&&(e={}),void 0===t&&(t=null),U(h({},e,{style:h({},e.style,(n={},n[r.j.arrowSize.var]=C?(0,s.px)(C):void 0,n[r.j.arrowShadowColor.var]=k,n))}),t)}),[U,C,k]),ee=c.useCallback((function(e,t){return void 0===e&&(e={}),void 0===t&&(t=null),h({ref:t},T,e,{id:B,role:"tooltip",style:h({},e.style,{position:"relative",transformOrigin:r.j.transformOrigin.varRef})})}),[T,B]);return{isOpen:A,show:G,hide:L,getTriggerProps:Q,getTooltipProps:ee,getTooltipPositionerProps:V,getArrowProps:X,getArrowInnerProps:M}}(h({},T,{direction:y.direction}));if((0,s.HD)(O)||C)A=c.createElement(i.m$.span,h({tabIndex:0},q.getTriggerProps()),O);else{var N=c.Children.only(O);A=c.cloneElement(N,q.getTriggerProps(N.props,N.ref))}var K=!!k,Z=q.getTooltipProps({},t),U=K?(0,s.CE)(Z,["role","id"]):Z,M=(0,s.ei)(Z,["role","id"]);return D?c.createElement(c.Fragment,null,A,c.createElement(u.M,null,q.isOpen&&c.createElement(o.h_,E,c.createElement(i.m$.div,h({},q.getTooltipPositionerProps(),{__css:{zIndex:j.zIndex,pointerEvents:"none"}}),c.createElement(b,h({variants:m},U,{initial:"exit",animate:"enter",exit:"exit",__css:j}),D,K&&c.createElement(a.TX,M,k),P&&c.createElement(i.m$.div,{"data-popper-arrow":!0,className:"chakra-tooltip__arrow-wrapper"},c.createElement(i.m$.div,{"data-popper-arrow-inner":!0,className:"chakra-tooltip__arrow",__css:{bg:j.bg}}))))))):c.createElement(c.Fragment,null,O)}));s.Ts&&(j.displayName="Tooltip")},6475:function(e,t,n){"use strict";n.d(t,{O:function(){return u}});var r=n(9499),o=n(2640);function i(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function s(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?i(Object(n),!0).forEach((function(t){(0,r.Z)(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}var a=n(7294),l=(a.useContext,a.useState);function u(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:null,t=l(e),n=(0,o.Z)(t,2),i=n[0],a=n[1];function u(e,t){var n=e.split(".").map((function(e){return e.trim()}));return function(e){a(s(s({},i),{},(0,r.Z)({},n[0],s(s({},i[n[0]]),{},(0,r.Z)({},n[1],e)))))}(t)}function c(){a(e)}return[i,u,c]}},9452:function(e,t,n){"use strict";n.r(t),n.d(t,{default:function(){return R}});var r=n(9499),o=n(29),i=n(2640),s=n(7794),a=n.n(s),l=n(7294),u=n(1163),c=n(5325),d=n(4626),p=n(6475),f=n(2775),h=n(313),v=n(3150),m=n(7375),g=n(8609),x=n(8527),b=n(5193),j=n(4612),w=n(2684),y=n(6886),O=n(7398),D=n(4809),C=n(2821),k=n(7516),P=n(3854),S=n(6415),E=n(5893);function _(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function z(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?_(Object(n),!0).forEach((function(t){(0,r.Z)(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):_(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}var I=(0,l.createContext)(),T=function(){return(0,l.useContext)(I)};function R(){var e=(0,c.a)(),t=e.isAuth,n=e.username,r=e.sessionID,s=(0,m.qY)(),v=(0,u.useRouter)(),j=(0,g.pm)(),w=(0,f.i)().innerWidth,y=(0,D.h4)({initialStep:0}),O=y.nextStep,C=(y.prevStep,y.setStep,y.reset),k=y.activeStep,P=(0,l.useState)(!1),_=P[0],T=P[1],R=(0,l.useState)(!1),Z=R[0],U=R[1],M=(0,l.useState)(null),X=M[0],B=M[1],F=(0,l.useState)(null),W=F[0],$=F[1],G=(0,p.O)({generateDocument:{document_name:"",writer:""},upload:{file:null}}),L=(0,i.Z)(G,3),Y=L[0],H=L[1],J=L[2],Q=[{label:"Generate Document",component:(0,E.jsx)(A,{})},{label:"Upload",component:(0,E.jsx)(q,{})},{label:"Done",component:(0,E.jsx)(N,{})}],V=(Q.length,Q.length-1);function ee(){return te.apply(this,arguments)}function te(){return(te=(0,o.Z)(a().mark((function e(){var t,n,o,i,s,l;return a().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return U(!0),e.prev=1,o=Y.generateDocument,i=o.document_name,s=o.writer,e.next=6,(0,d.W)("post",S.zD.GENERATE_CODE)({writer:s,session_id:r,doc_name:i});case 6:if("failed"!==(null===(l=e.sent)||void 0===l||null===(t=l.data)||void 0===t?void 0:t.status)){e.next=9;break}throw null===l||void 0===l||null===(n=l.data)||void 0===n?void 0:n.msg;case 9:B(null===l||void 0===l?void 0:l.data),O(),e.next=16;break;case 13:e.prev=13,e.t0=e.catch(1),j({title:"Generate failed.",description:e.t0.toString(),status:"error",position:"top",duration:9e3,isClosable:!0});case 16:U(!1);case 17:case"end":return e.stop()}}),e,null,[[1,13]])})))).apply(this,arguments)}var ne=function(){var e=(0,o.Z)(a().mark((function e(){var t,n,o,i,s,l,u;return a().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:if(U(!0),e.prev=1,null!==Y.upload.file){e.next=4;break}throw"You should use a file to upload it.";case 4:if("undefined"!==typeof X.code){e.next=6;break}throw"Document code not found.";case 6:return o=X.code,i=Y.upload.file,e.next=10,i.arrayBuffer();case 10:return s=e.sent,l={buffer:new Uint8Array(s),extension:i.name.split(".").at(-1)},e.next=14,(0,d.W)("post",S.zD.UPLOAD)(l.buffer,{code:o,extension:l.extension,session_id:r,"Content-type":"application/text"});case 14:if("failed"!==(null===(u=e.sent)||void 0===u||null===(t=u.data)||void 0===t?void 0:t.status)){e.next=17;break}throw null===u||void 0===u||null===(n=u.data)||void 0===n?void 0:n.msg;case 17:$(null===u||void 0===u?void 0:u.data),O(),e.next=24;break;case 21:e.prev=21,e.t0=e.catch(1),j({title:"Generate failed.",description:e.t0.toString(),status:"error",position:"top",duration:9e3,isClosable:!0});case 24:U(!1);case 25:case"end":return e.stop()}}),e,null,[[1,21]])})));return function(){return e.apply(this,arguments)}}();return(0,l.useEffect)((function(){t||v.replace("/auth/login")})),(0,E.jsxs)(I.Provider,{value:z(z(z({generateDoc:ee,generatedDocResult:X,uploadDoc:ne,uploadDocResult:W,stateSetter:H},Y.generateDocument),Y.upload),{},{setIsNextButtonDisabled:T,stateResetter:J,stepsResetter:C}),children:[(0,E.jsxs)(x.Kq,{w:"full",h:"full",spacing:6,padding:4,justify:"space-around",align:"center",children:[(0,E.jsxs)(x.X6,{children:["Welcome,"," ",null===n||void 0===n?void 0:n.split("").map((function(e,t){return 0===t?e.toUpperCase():e})).join("")]}),(0,E.jsx)(x.Kq,{w:"full",h:"full",children:(0,E.jsx)(D.Rg,{activeStep:k,labelOrientation:"vertical",children:Q.map((function(e,t){var n=e.label,r=e.component;return(0,E.jsx)(D.h8,{alignSelf:w>h.Ei?"center":"auto",label:n,children:(0,E.jsx)(x.Kq,{w:"full",h:"full",align:"center",justify:"center",children:r})},t)}))})}),(0,E.jsx)(x.Ug,{alignSelf:"end",children:(0,E.jsx)(b.zx,{isDisabled:_,isLoading:Z,size:"sm",onClick:(0,o.Z)(a().mark((function e(){return a().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:e.t0=k,e.next=0===e.t0?3:1===e.t0?6:e.t0===V?9:11;break;case 3:return e.next=5,ee();case 5:case 8:case 11:return e.abrupt("break",12);case 6:return e.next=8,ne();case 9:return s.onOpen(),e.abrupt("break",12);case 12:case"end":return e.stop()}}),e)}))),children:k===Q.length-1?"Finish":"Next"})})]}),(0,E.jsx)(K,{disclosure:s})]})}function A(){var e=T(),t=e.document_name,n=e.writer,r=e.stateSetter,o=e.setIsNextButtonDisabled;return(0,l.useEffect)((function(){[t,n].filter((function(e){return!e})).length>0?o(!0):o(!1)}),[t,n]),(0,E.jsxs)(x.Kq,{spacing:6,my:6,maxW:80,children:[(0,E.jsxs)(x.Kq,{w:"full",spacing:1,children:[(0,E.jsx)(x.Ug,{align:"end",children:(0,E.jsx)(x.X6,{fontSize:13,children:"Document Name"})}),(0,E.jsx)(j.II,{variant:"filled",value:t,onChange:function(e){return r("generateDocument.document_name",e.target.value)}})]}),(0,E.jsxs)(x.Kq,{children:[(0,E.jsx)(x.Ug,{align:"end",children:(0,E.jsx)(x.X6,{fontSize:13,children:"Writer"})}),(0,E.jsx)(j.II,{variant:"filled",value:n,onChange:function(e){return r("generateDocument.writer",e.target.value)}})]})]})}function q(){var e,t=T(),n=t.stateSetter,o=t.file,i=t.setIsNextButtonDisabled,s=t.generatedDocResult,a=(0,w.Sx)({xs:"sm",md:"lg"});return(0,l.useEffect)((function(){i(null===o)}),[o]),(0,E.jsxs)(E.Fragment,{children:[!s.code&&(0,E.jsxs)(y.bZ,{status:"error",children:[(0,E.jsx)(y.zM,{}),(0,E.jsxs)("div",{children:["Document code not found, Please"," ",(0,E.jsx)("span",{style:{textDecoration:"underline",cursor:"pointer"},onClick:function(){return router.push("/support")},children:"contact us"})," ","to solve this problem."]})]}),(0,E.jsxs)(x.Kq,{w:"full",h:"full",spacing:6,justify:"center",align:"center",children:[s.code&&(0,E.jsxs)(x.Kq,{spacing:0,children:[(0,E.jsx)(x.X6,{opacity:.5,fontSize:"x-small",children:"Document code"}),(0,E.jsx)(x.X6,{children:s.code})]}),(0,E.jsxs)(x.Kq,{align:"center",children:[(0,E.jsx)(x.xv,{children:"Please, Insert Document code inside the document, then upload it below."}),(0,E.jsxs)(x.Ug,{children:[(0,E.jsx)(O.u,{hasArrow:!0,placement:"top",label:"Import the document which you need to upload",children:(0,E.jsxs)(x.xu,{position:"relative",w:"max-content",children:[(0,E.jsx)(b.zx,{size:a,rightIcon:(0,E.jsx)(C.aBR,{size:"1.4em"}),children:"Upload File"}),(0,E.jsx)(j.II,(e={w:"full",type:"file",left:0,onChange:function(e){var t=e.target.files[0];"undefined"!==typeof t&&n("upload.file",t)},top:0,cursor:"pointer",position:"absolute",opacity:0},(0,r.Z)(e,"w","full"),(0,r.Z)(e,"h","full"),e))]})}),(0,E.jsx)(x.Ug,{justify:"end",align:"end",w:"full",children:o&&(0,E.jsx)(v.UL,{file:o})})]})]})]})]})}function N(){var e=(0,u.useRouter)(),t=T().uploadDocResult;return(0,E.jsxs)(x.Kq,{align:"center",children:[["code"].filter((function(e){return!t.hasOwnProperty(e)&&e.trim().length>0})).length>0&&(0,E.jsxs)(y.bZ,{status:"error",children:[(0,E.jsx)(y.zM,{}),(0,E.jsxs)("div",{children:["Some data isn't found, Please"," ",(0,E.jsx)("span",{style:{textDecoration:"underline",cursor:"pointer"},onClick:function(){return e.push("/support")},children:"contact us"})," ","to solve this problem."]})]}),(0,E.jsx)(x.xv,{fontSize:18,children:"The document is uploaded successfully !"}),(0,E.jsx)(x.iz,{}),(0,E.jsx)(x.Kq,{children:(0,E.jsxs)(x.Kq,{spacing:.5,children:[(0,E.jsx)(x.X6,{opacity:.5,fontSize:10,textTransform:"capitalize",children:"document code"}),(0,E.jsx)(x.X6,{size:"lg",children:t.code||"Error: Not found"})]})})]})}function K(e){var t=e.disclosure,n=(0,u.useRouter)(),r=T(),o=r.stateResetter,i=r.stepsResetter;return(0,E.jsx)(v.aR,z(z({},t),{},{header:"Submit more? ",footer:(0,E.jsxs)(x.Ug,{justify:"space-between",w:"full",children:[(0,E.jsx)(b.zx,{leftIcon:(0,E.jsx)(k.DaR,{size:"1.4em"}),size:"sm",onClick:function(){return n.push("/")},children:"Back Home"}),(0,E.jsx)(b.zx,{size:"sm",leftIcon:(0,E.jsx)(P.KbN,{size:"1.4em"}),onClick:function(){o(),i(),t.onClose()},children:"Submit more"})]}),children:(0,E.jsxs)(x.xv,{children:["Do you want to submit more documents or that","'","s enough for this day?"]})}))}},1518:function(e,t,n){(window.__NEXT_P=window.__NEXT_P||[]).push(["/submit",function(){return n(9452)}])},2640:function(e,t,n){"use strict";function r(e,t){(null==t||t>e.length)&&(t=e.length);for(var n=0,r=new Array(t);n<t;n++)r[n]=e[n];return r}function o(e,t){return function(e){if(Array.isArray(e))return e}(e)||function(e,t){var n=null==e?null:"undefined"!==typeof Symbol&&e[Symbol.iterator]||e["@@iterator"];if(null!=n){var r,o,i=[],s=!0,a=!1;try{for(n=n.call(e);!(s=(r=n.next()).done)&&(i.push(r.value),!t||i.length!==t);s=!0);}catch(l){a=!0,o=l}finally{try{s||null==n.return||n.return()}finally{if(a)throw o}}return i}}(e,t)||function(e,t){if(e){if("string"===typeof e)return r(e,t);var n=Object.prototype.toString.call(e).slice(8,-1);return"Object"===n&&e.constructor&&(n=e.constructor.name),"Map"===n||"Set"===n?Array.from(e):"Arguments"===n||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)?r(e,t):void 0}}(e,t)||function(){throw new TypeError("Invalid attempt to destructure non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}()}n.d(t,{Z:function(){return o}})}},function(e){e.O(0,[612,774,888,179],(function(){return t=1518,e(e.s=t);var t}));var t=e.O();_N_E=t}]);