;(function(window){
    var Sign = {
        /**
         * 签名插件
         * 算法: ①请求参数abc排序 ②取值拼接 ③在拼接上密钥(secret)的json数组字符串 ④最后md5加密
         * 例子①: {"username":"Jason", "sysCode:"sso", "timestamp":12874613157} 密钥:123
         * ②排序后: {"sysCode:"sso", "timestamp":12874613157, "username":"Jason"}
         * ③拼接后: sso12874613157Jason["123"]
         * ④加密后：
         * @param map 請求參數
         * @param salt 密钥
         * @returns string 签名
         */
        sign:function(map, salt) {
            var array = [];
            for (var key in map) {
                array.push(key);
            }
            array.sort();
            var sign = "";
            array.forEach(function (o) {
                sign += map[o];
            });
            sign += JSON.stringify(salt)
            return md5(sign);
        }
    }
    window.Sign = Sign;
})(window);