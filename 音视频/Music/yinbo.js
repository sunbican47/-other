	var wrap = document.getElementById("canvas");
    var cxt = wrap.getContext("2d");
    //��ȡAPI
    var AudioContext=AudioContext||webkitAudioContext;
    var context=new AudioContext;
    //�����ڵ�
    var source = context.createMediaElementSource(audio);
    var analyser = context.createAnalyser();
    //���ӣ�source �� analyser �� destination
    source.connect(analyser);
    analyser.connect(context.destination);
    //��������
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
        analyser.getByteFrequencyData(output);//��ȡƵ������
        cxt.clearRect(0,0, wrap.width, wrap.height);
        for (var i = 0; i < 10; i++) {
            var value = output[i] / 10;//<===��ȡ����
            console.info(value);
            var heights=wrap.height-(value*2);
            //����������
            cxt.fillStyle="#ff0";
            cxt.strokeRect(i*20,heights,20,value*2);
            cxt.fillRect(i*20,heights,20,value*2);
            cxt.closePath();
            //��ʵ��Բ
            cxt.beginPath();
            cxt.fillStyle="#f00";
            cxt.arc(10+i*20,heights-(value*2), status, 0, 2*Math.PI);//����Բ
            cxt.fill();//��ʵ��Բ
            cxt.stroke();
            cxt.closePath();
        }
        //������һ֡
        requestAnimationFrame(drawSpectrum);
    })();