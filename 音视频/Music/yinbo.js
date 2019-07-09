	var wrap = document.getElementById("canvas");
    var cxt = wrap.getContext("2d");
    //获取API
    var AudioContext=AudioContext||webkitAudioContext;
    var context=new AudioContext;
    //创建节点
    var source = context.createMediaElementSource(audio);
    var analyser = context.createAnalyser();
    //连接：source → analyser → destination
    source.connect(analyser);
    analyser.connect(context.destination);
    //创建数据
    var output = new Uint8Array(analyser.fftSize);
    console.info(output);
    var status=0;
    document.getElementById("audio").addEventListener("playing", function(){
       status=5;
    });
    document.getElementById("audio").addEventListener("pause", function(){
        status=0;
    });
    (function drawSpectrum() {
        analyser.getByteFrequencyData(output);//获取频域数据
        cxt.clearRect(0,0, wrap.width, wrap.height);
        for (var i = 0; i < 10; i++) {
            var value = output[i] / 10;//<===获取数据
            console.info(value);
            var heights=wrap.height-(value*2);
            //画跳动条柱
            cxt.fillStyle="#ff0";
            cxt.strokeRect(i*20,heights,20,value*2);
            cxt.fillRect(i*20,heights,20,value*2);
            cxt.closePath();
            //画实心圆
            cxt.beginPath();
            cxt.fillStyle="#f00";
            cxt.arc(10+i*20,heights-(value*2), status, 0, 2*Math.PI);//整个圆
            cxt.fill();//画实心圆
            cxt.stroke();
            cxt.closePath();
        }
        //请求下一帧
        requestAnimationFrame(drawSpectrum);
    })();