function outer(s) {
    var x = arguments[0];           // lexical scoping
    if(s.indexOf('o') > 0) {        // "method" calls
        function inner(y) {
            var t = ".suffix";      // copy propagation
            var arr = [x + t, y];   // object creation
            this.data = arr;
        }
        return new inner(s);        // opject creation
    }
}
var outerProp = outer("outer").data; // prototype chain