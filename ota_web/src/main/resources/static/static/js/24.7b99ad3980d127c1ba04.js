webpackJsonp([24],{1227:function(i,t,n){var e=n(940);"string"==typeof e&&(e=[[i.i,e,""]]),e.locals&&(i.exports=e.locals);n(573)("332ed5f6",e,!0)},1313:function(i,t){i.exports={render:function(){var i=this,t=i.$createElement,n=i._self._c||t;return n("div",{staticClass:"login-container"},[n("el-form",{ref:"loginForm",staticClass:"card-box login-form",attrs:{autoComplete:"off",model:i.loginForm,rules:i.loginRules,"label-position":"left","label-width":"0px"}},[n("h3",{staticClass:"title"},[i._v("平台登录")]),i._v(" "),n("el-form-item",{attrs:{prop:"name"}},[n("span",{staticClass:"svg-container"},[n("icon-svg",{attrs:{"icon-class":"xinrenzhinan"}})],1),i._v(" "),n("el-input",{attrs:{name:"name",type:"text",autoComplete:"off",placeholder:"用户名"},model:{value:i.loginForm.name,callback:function(t){i.loginForm.name=t},expression:"loginForm.name"}})],1),i._v(" "),n("el-form-item",{attrs:{prop:"password"}},[n("span",{staticClass:"svg-container"},[n("icon-svg",{attrs:{"icon-class":"mima"}})],1),i._v(" "),n("el-input",{attrs:{name:"password",type:"password",autoComplete:"off",placeholder:"密码"},model:{value:i.loginForm.password,callback:function(t){i.loginForm.password=t},expression:"loginForm.password"}})],1),i._v(" "),n("el-form-item",{staticClass:"code-box",attrs:{prop:"validCode"}},[n("span",{staticClass:"svg-container"},[n("icon-svg",{attrs:{"icon-class":"bug"}})],1),i._v(" "),n("el-input",{staticClass:"valid-code",attrs:{name:"validCode",autoComplete:"off",placeholder:"验证码"},model:{value:i.loginForm.validCode,callback:function(t){i.loginForm.validCode=t},expression:"loginForm.validCode"}}),i._v(" "),n("div",{attrs:{id:"code-wrap"}},[n("img",{attrs:{src:i.imgsrc,alt:""}}),i._v(" "),n("span",{on:{click:i.refreshCode}},[i._v("换一张")])])],1),i._v(" "),n("el-form-item",[n("el-button",{staticStyle:{width:"100%"},attrs:{type:"primary",loading:i.loading},nativeOn:{click:function(t){t.preventDefault(),i.handleLogin(t)}}},[i._v("\n        登录\n      ")])],1),i._v(" "),n("div",{staticClass:"tips"},[n("router-link",{staticClass:"fr",attrs:{to:"/regist"}},[i._v("没有账号？立即注册")])],1)],1)],1)},staticRenderFns:[]}},614:function(i,t,n){function e(i){n(1227)}var r=n(21)(n(878),n(1313),e,null,null);i.exports=r.exports},652:function(i,t,n){"use strict";function e(i){return/^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/.test(i)}function r(i){return/^1[34578]\d{9}$/.test(i)}function o(i){return/^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,16}$/.test(i)}t.a=e,t.b=r,t.c=o},878:function(i,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var e=(n(652),n(232)),r=n(894);n.n(r);t.default={components:{},name:"login",data:function(){return{loginForm:{name:"weiqiang",password:"123456abc#",validCode:""},loginRules:{name:[{required:!0,trigger:"blur",message:"必填"}],password:[{required:!0,trigger:"blur",message:"必填"}],validCode:[{required:!0,trigger:"blur",message:"必填"}]},loading:!1,imgsrc:"/ota/validCode/get"}},methods:{refreshCode:function(){var i=+new Date,t=this.imgsrc;this.imgsrc=t+"?timestamp="+i},handleLogin:function(){var i=this;this.$refs.loginForm.validate(function(t){if(!t)return console.log("error submit!!"),i.loading=!1,!1;n.i(e.a)().then(function(t){var n=i.loginForm.password,e=t.data;if("0000"!=e.code)return void i.$message(e.data);console.log(e);var r=e.exponent,o=e.modulus,s=e.privateKeyId,a=RSAUtils.getKeyPair(r,"",o),g=RSAUtils.encryptedString(a,n);i.loading=!0;var d=i.loginForm,l=d.name,u=d.validCode;i.$store.dispatch("Login",{name:l,password:g,validCode:u,privateKeyId:s}).then(function(t){i.loading=!1,"0000"==t.code?i.$router.push({path:"/"}):i.$message(t.message)}).catch(function(t){i.$message.error(t),i.loading=!1})})})}}}},894:function(t,n){!function(t){function n(i){var t=o,n=t.biDivideByRadixPower(i,this.k-1),e=t.biMultiply(n,this.mu),r=t.biDivideByRadixPower(e,this.k+1),s=t.biModuloByRadixPower(i,this.k+1),a=t.biMultiply(r,this.modulus),g=t.biModuloByRadixPower(a,this.k+1),d=t.biSubtract(s,g);d.isNeg&&(d=t.biAdd(d,this.bkplus1));for(var l=t.biCompare(d,this.modulus)>=0;l;)d=t.biSubtract(d,this.modulus),l=t.biCompare(d,this.modulus)>=0;return d}function e(i,t){var n=o.biMultiply(i,t);return this.modulo(n)}function r(i,t){var n=new l;n.digits[0]=1;for(var e=i,r=t;;){if(0!=(1&r.digits[0])&&(n=this.multiplyMod(n,e)),r=o.biShiftRight(r,1),0==r.digits[0]&&0==o.biHighIndex(r))break;e=this.multiplyMod(e,e)}return n}if(void 0===t.RSAUtils)var o=t.RSAUtils={};var s,a,g,d,l=t.BigInt=function(i){this.digits="boolean"==typeof i&&1==i?null:a.slice(0),this.isNeg=!1};o.setMaxDigits=function(i){s=i,a=new Array(s);for(var t=0;t<a.length;t++)a[t]=0;g=new l,d=new l,d.digits[0]=1},o.setMaxDigits(20);o.biFromNumber=function(i){var t=new l;t.isNeg=i<0,i=Math.abs(i);for(var n=0;i>0;)t.digits[n++]=65535&i,i=Math.floor(i/65536);return t};var u=o.biFromNumber(1e15);o.biFromDecimal=function(i){for(var t,n="-"==i.charAt(0),e=n?1:0;e<i.length&&"0"==i.charAt(e);)++e;if(e==i.length)t=new l;else{var r=i.length-e,s=r%15;for(0==s&&(s=15),t=o.biFromNumber(Number(i.substr(e,s))),e+=s;e<i.length;)t=o.biAdd(o.biMultiply(t,u),o.biFromNumber(Number(i.substr(e,15)))),e+=15;t.isNeg=n}return t},o.biCopy=function(i){var t=new l(!0);return t.digits=i.digits.slice(0),t.isNeg=i.isNeg,t},o.reverseStr=function(i){for(var t="",n=i.length-1;n>-1;--n)t+=i.charAt(n);return t};var A=["0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"];o.biToString=function(i,t){var n=new l;n.digits[0]=t;for(var e=o.biDivideModulo(i,n),r=A[e[1].digits[0]];1==o.biCompare(e[0],g);)e=o.biDivideModulo(e[0],n),digit=e[1].digits[0],r+=A[e[1].digits[0]];return(i.isNeg?"-":"")+o.reverseStr(r)},o.biToDecimal=function(i){var t=new l;t.digits[0]=10;for(var n=o.biDivideModulo(i,t),e=String(n[1].digits[0]);1==o.biCompare(n[0],g);)n=o.biDivideModulo(n[0],t),e+=String(n[1].digits[0]);return(i.isNeg?"-":"")+o.reverseStr(e)};var c=["0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"];o.digitToHex=function(t){var n="";for(i=0;i<4;++i)n+=c[15&t],t>>>=4;return o.reverseStr(n)},o.biToHex=function(i){for(var t="",n=(o.biHighIndex(i),o.biHighIndex(i));n>-1;--n)t+=o.digitToHex(i.digits[n]);return t},o.charToHex=function(i){return i>=48&&i<=57?i-48:i>=65&&i<=90?10+i-65:i>=97&&i<=122?10+i-97:0},o.hexToDigit=function(i){for(var t=0,n=Math.min(i.length,4),e=0;e<n;++e)t<<=4,t|=o.charToHex(i.charCodeAt(e));return t},o.biFromHex=function(i){for(var t=new l,n=i.length,e=n,r=0;e>0;e-=4,++r)t.digits[r]=o.hexToDigit(i.substr(Math.max(e-4,0),Math.min(e,4)));return t},o.biFromString=function(i,t){var n="-"==i.charAt(0),e=n?1:0,r=new l,s=new l;s.digits[0]=1;for(var a=i.length-1;a>=e;a--){var g=i.charCodeAt(a),d=o.charToHex(g),u=o.biMultiplyDigit(s,d);r=o.biAdd(r,u),s=o.biMultiplyDigit(s,t)}return r.isNeg=n,r},o.biDump=function(i){return(i.isNeg?"-":"")+i.digits.join(" ")},o.biAdd=function(i,t){var n;if(i.isNeg!=t.isNeg)t.isNeg=!t.isNeg,n=o.biSubtract(i,t),t.isNeg=!t.isNeg;else{n=new l;for(var e,r=0,s=0;s<i.digits.length;++s)e=i.digits[s]+t.digits[s]+r,n.digits[s]=e%65536,r=Number(e>=65536);n.isNeg=i.isNeg}return n},o.biSubtract=function(i,t){var n;if(i.isNeg!=t.isNeg)t.isNeg=!t.isNeg,n=o.biAdd(i,t),t.isNeg=!t.isNeg;else{n=new l;var e,r;r=0;for(var s=0;s<i.digits.length;++s)e=i.digits[s]-t.digits[s]+r,n.digits[s]=e%65536,n.digits[s]<0&&(n.digits[s]+=65536),r=0-Number(e<0);if(-1==r){r=0;for(var s=0;s<i.digits.length;++s)e=0-n.digits[s]+r,n.digits[s]=e%65536,n.digits[s]<0&&(n.digits[s]+=65536),r=0-Number(e<0);n.isNeg=!i.isNeg}else n.isNeg=i.isNeg}return n},o.biHighIndex=function(i){for(var t=i.digits.length-1;t>0&&0==i.digits[t];)--t;return t},o.biNumBits=function(i){var t,n=o.biHighIndex(i),e=i.digits[n],r=16*(n+1);for(t=r;t>r-16&&0==(32768&e);--t)e<<=1;return t},o.biMultiply=function(i,t){for(var n,e,r,s=new l,a=o.biHighIndex(i),g=o.biHighIndex(t),d=0;d<=g;++d){for(n=0,r=d,j=0;j<=a;++j,++r)e=s.digits[r]+i.digits[j]*t.digits[d]+n,s.digits[r]=65535&e,n=e>>>16;s.digits[d+a+1]=n}return s.isNeg=i.isNeg!=t.isNeg,s},o.biMultiplyDigit=function(i,t){var n,e,r;result=new l,n=o.biHighIndex(i),e=0;for(var s=0;s<=n;++s)r=result.digits[s]+i.digits[s]*t+e,result.digits[s]=65535&r,e=r>>>16;return result.digits[1+n]=e,result},o.arrayCopy=function(i,t,n,e,r){for(var o=Math.min(t+r,i.length),s=t,a=e;s<o;++s,++a)n[a]=i[s]};var p=[0,32768,49152,57344,61440,63488,64512,65024,65280,65408,65472,65504,65520,65528,65532,65534,65535];o.biShiftLeft=function(i,t){var n=Math.floor(t/16),e=new l;o.arrayCopy(i.digits,0,e.digits,n,e.digits.length-n);for(var r=t%16,s=16-r,a=e.digits.length-1,g=a-1;a>0;--a,--g)e.digits[a]=e.digits[a]<<r&65535|(e.digits[g]&p[r])>>>s;return e.digits[0]=e.digits[a]<<r&65535,e.isNeg=i.isNeg,e};var f=[0,1,3,7,15,31,63,127,255,511,1023,2047,4095,8191,16383,32767,65535];o.biShiftRight=function(i,t){var n=Math.floor(t/16),e=new l;o.arrayCopy(i.digits,n,e.digits,0,i.digits.length-n);for(var r=t%16,s=16-r,a=0,g=a+1;a<e.digits.length-1;++a,++g)e.digits[a]=e.digits[a]>>>r|(e.digits[g]&f[r])<<s;return e.digits[e.digits.length-1]>>>=r,e.isNeg=i.isNeg,e},o.biMultiplyByRadixPower=function(i,t){var n=new l;return o.arrayCopy(i.digits,0,n.digits,t,n.digits.length-t),n},o.biDivideByRadixPower=function(i,t){var n=new l;return o.arrayCopy(i.digits,t,n.digits,0,n.digits.length-t),n},o.biModuloByRadixPower=function(i,t){var n=new l;return o.arrayCopy(i.digits,0,n.digits,0,t),n},o.biCompare=function(i,t){if(i.isNeg!=t.isNeg)return 1-2*Number(i.isNeg);for(var n=i.digits.length-1;n>=0;--n)if(i.digits[n]!=t.digits[n])return i.isNeg?1-2*Number(i.digits[n]>t.digits[n]):1-2*Number(i.digits[n]<t.digits[n]);return 0},o.biDivideModulo=function(i,t){var n,e,r=o.biNumBits(i),s=o.biNumBits(t),a=t.isNeg;if(r<s)return i.isNeg?(n=o.biCopy(d),n.isNeg=!t.isNeg,i.isNeg=!1,t.isNeg=!1,e=biSubtract(t,i),i.isNeg=!0,t.isNeg=a):(n=new l,e=o.biCopy(i)),[n,e];n=new l,e=i;for(var g=Math.ceil(s/16)-1,u=0;t.digits[g]<32768;)t=o.biShiftLeft(t,1),++u,++s,g=Math.ceil(s/16)-1;e=o.biShiftLeft(e,u),r+=u;for(var A=Math.ceil(r/16)-1,c=o.biMultiplyByRadixPower(t,A-g);-1!=o.biCompare(e,c);)++n.digits[A-g],e=o.biSubtract(e,c);for(var p=A;p>g;--p){var f=p>=e.digits.length?0:e.digits[p],b=p-1>=e.digits.length?0:e.digits[p-1],h=p-2>=e.digits.length?0:e.digits[p-2],m=g>=t.digits.length?0:t.digits[g],v=g-1>=t.digits.length?0:t.digits[g-1];n.digits[p-g-1]=f==m?65535:Math.floor((65536*f+b)/m);for(var C=n.digits[p-g-1]*(65536*m+v),x=4294967296*f+(65536*b+h);C>x;)--n.digits[p-g-1],C=n.digits[p-g-1]*(65536*m|v),x=65536*f*65536+(65536*b+h);c=o.biMultiplyByRadixPower(t,p-g-1),e=o.biSubtract(e,o.biMultiplyDigit(c,n.digits[p-g-1])),e.isNeg&&(e=o.biAdd(e,c),--n.digits[p-g-1])}return e=o.biShiftRight(e,u),n.isNeg=i.isNeg!=a,i.isNeg&&(n=a?o.biAdd(n,d):o.biSubtract(n,d),t=o.biShiftRight(t,u),e=o.biSubtract(t,e)),0==e.digits[0]&&0==o.biHighIndex(e)&&(e.isNeg=!1),[n,e]},o.biDivide=function(i,t){return o.biDivideModulo(i,t)[0]},o.biModulo=function(i,t){return o.biDivideModulo(i,t)[1]},o.biMultiplyMod=function(i,t,n){return o.biModulo(o.biMultiply(i,t),n)},o.biPow=function(i,t){for(var n=d,e=i;;){if(0!=(1&t)&&(n=o.biMultiply(n,e)),0==(t>>=1))break;e=o.biMultiply(e,e)}return n},o.biPowMod=function(i,t,n){for(var e=d,r=i,s=t;;){if(0!=(1&s.digits[0])&&(e=o.biMultiplyMod(e,r,n)),s=o.biShiftRight(s,1),0==s.digits[0]&&0==o.biHighIndex(s))break;r=o.biMultiplyMod(r,r,n)}return e},t.BarrettMu=function(i){this.modulus=o.biCopy(i),this.k=o.biHighIndex(this.modulus)+1;var t=new l;t.digits[2*this.k]=1,this.mu=o.biDivide(t,this.modulus),this.bkplus1=new l,this.bkplus1.digits[this.k+1]=1,this.modulo=n,this.multiplyMod=e,this.powMod=r};var b=function(i,n,e){var r=o;this.e=r.biFromHex(i),this.d=r.biFromHex(n),this.m=r.biFromHex(e),this.chunkSize=2*r.biHighIndex(this.m),this.radix=16,this.barrett=new t.BarrettMu(this.m)};o.getKeyPair=function(i,t,n){return new b(i,t,n)},void 0===t.twoDigit&&(t.twoDigit=function(i){return(i<10?"0":"")+String(i)}),o.encryptedString=function(i,t){for(var n=[],e=t.length,r=0;r<e;)n[r]=t.charCodeAt(r),r++;for(;n.length%i.chunkSize!=0;)n[r++]=0;var s,a,g,d=n.length,u="";for(r=0;r<d;r+=i.chunkSize){for(g=new l,s=0,a=r;a<r+i.chunkSize;++s)g.digits[s]=n[a++],g.digits[s]+=n[a++]<<8;var A=i.barrett.powMod(g,i.e);u+=(16==i.radix?o.biToHex(A):o.biToString(A,i.radix))+" "}return u.substring(0,u.length-1)},o.decryptedString=function(i,t){var n,e,r,s=t.split(" "),a="";for(n=0;n<s.length;++n){var g;for(g=16==i.radix?o.biFromHex(s[n]):o.biFromString(s[n],i.radix),r=i.barrett.powMod(g,i.d),e=0;e<=o.biHighIndex(r);++e)a+=String.fromCharCode(255&r.digits[e],r.digits[e]>>8)}return 0==a.charCodeAt(a.length-1)&&(a=a.substring(0,a.length-1)),a},o.setMaxDigits(130)}(window)},940:function(i,t,n){t=i.exports=n(572)(!0),t.push([i.i,".tips{font-size:14px;color:#fff;margin-bottom:5px}.login-container{position:relative;width:100%;height:100%;height:100vh;background-color:#2d3a4b}.login-container input:-webkit-autofill{-webkit-box-shadow:0 0 0 1000px #293444 inset!important;-webkit-text-fill-color:#fff!important}.login-container input{background:transparent;border:0;-webkit-appearance:none;border-radius:0;padding:12px 5px 12px 15px;color:#eee;height:47px}.login-container .el-input{display:inline-block;height:47px;width:85%}.login-container .el-input.valid-code{width:100px;border-right:1px solid #3e4650}.login-container .code-box{position:relative}.login-container #code-wrap{position:absolute;right:10px;top:5px;color:#20a0ff}.login-container #code-wrap img{height:36px;vertical-align:middle;margin-right:10px}.login-container #code-wrap span{cursor:pointer}.login-container .svg-container{padding:6px 5px 6px 15px;color:#889aa4}.login-container .title{font-size:26px;font-weight:400;color:#eee;margin:0 auto 40px;text-align:center;font-weight:700}.login-container .login-form{position:absolute;left:0;right:0;width:400px;padding:35px 35px 15px;margin:120px auto}.login-container .el-form-item{border:1px solid hsla(0,0%,100%,.1);background:rgba(0,0,0,.1);border-radius:5px;color:#454545}.login-container .forget-pwd{color:#fff}","",{version:3,sources:["E:/dev/study/admin/src/views/login/index.vue"],names:[],mappings:"AACA,MACE,eAAgB,AAChB,WAAY,AACZ,iBAAmB,CACpB,AACD,iBACE,kBAAmB,AACnB,WAAY,AACZ,YAAa,AACb,aAAc,AACd,wBAA0B,CAC3B,AACD,wCACI,wDAA4D,AAC5D,sCAAyC,CAC5C,AACD,uBACI,uBAAwB,AACxB,SAAY,AACZ,wBAAyB,AACzB,gBAAmB,AACnB,2BAA4B,AAC5B,WAAe,AACf,WAAa,CAChB,AACD,2BACI,qBAAsB,AACtB,YAAa,AACb,SAAW,CACd,AACD,sCACI,YAAa,AACb,8BAAgC,CACnC,AACD,2BACI,iBAAmB,CACtB,AACD,4BACI,kBAAmB,AACnB,WAAY,AACZ,QAAS,AACT,aAAe,CAClB,AACD,gCACM,YAAa,AACb,sBAAuB,AACvB,iBAAmB,CACxB,AACD,iCACM,cAAgB,CACrB,AACD,gCACI,yBAA0B,AAC1B,aAAe,CAClB,AACD,wBACI,eAAgB,AAChB,gBAAiB,AACjB,WAAe,AACf,mBAA2B,AAC3B,kBAAmB,AACnB,eAAkB,CACrB,AACD,6BACI,kBAAmB,AACnB,OAAQ,AACR,QAAS,AACT,YAAa,AACb,uBAA6B,AAC7B,iBAAmB,CACtB,AACD,+BACI,oCAA2C,AAC3C,0BAA+B,AAC/B,kBAAmB,AACnB,aAAe,CAClB,AACD,6BACI,UAAY,CACf",file:"index.vue",sourcesContent:["\n.tips {\n  font-size: 14px;\n  color: #fff;\n  margin-bottom: 5px;\n}\n.login-container {\n  position: relative;\n  width: 100%;\n  height: 100%;\n  height: 100vh;\n  background-color: #2d3a4b;\n}\n.login-container input:-webkit-autofill {\n    -webkit-box-shadow: 0 0 0px 1000px #293444 inset !important;\n    -webkit-text-fill-color: #fff !important;\n}\n.login-container input {\n    background: transparent;\n    border: 0px;\n    -webkit-appearance: none;\n    border-radius: 0px;\n    padding: 12px 5px 12px 15px;\n    color: #eeeeee;\n    height: 47px;\n}\n.login-container .el-input {\n    display: inline-block;\n    height: 47px;\n    width: 85%;\n}\n.login-container .el-input.valid-code {\n    width: 100px;\n    border-right: 1px #3e4650 solid;\n}\n.login-container .code-box {\n    position: relative;\n}\n.login-container #code-wrap {\n    position: absolute;\n    right: 10px;\n    top: 5px;\n    color: #20a0ff;\n}\n.login-container #code-wrap img {\n      height: 36px;\n      vertical-align: middle;\n      margin-right: 10px;\n}\n.login-container #code-wrap span {\n      cursor: pointer;\n}\n.login-container .svg-container {\n    padding: 6px 5px 6px 15px;\n    color: #889aa4;\n}\n.login-container .title {\n    font-size: 26px;\n    font-weight: 400;\n    color: #eeeeee;\n    margin: 0px auto 40px auto;\n    text-align: center;\n    font-weight: bold;\n}\n.login-container .login-form {\n    position: absolute;\n    left: 0;\n    right: 0;\n    width: 400px;\n    padding: 35px 35px 15px 35px;\n    margin: 120px auto;\n}\n.login-container .el-form-item {\n    border: 1px solid rgba(255, 255, 255, 0.1);\n    background: rgba(0, 0, 0, 0.1);\n    border-radius: 5px;\n    color: #454545;\n}\n.login-container .forget-pwd {\n    color: #fff;\n}\n"],sourceRoot:""}])}});
//# sourceMappingURL=24.7b99ad3980d127c1ba04.js.map