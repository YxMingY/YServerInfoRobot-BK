<?php
require_once "Socket/socket_h.php";
require_once "api/qqclass.php";
const GROUP_QQ_NUM = 123;
const BOT_QQ_NUM = 456;
echo "[1]初始化qq机器人接口....".PHP_EOL;
$robot = array(
	'qq' => BOT_QQ_NUM, //机器人QQ号码
	'ip' => '127.0.0.1', //接口IP
	'port' => '10429', //接口端口
	'pass' => '', //密码
);
QQROT::init($robot['qq'], $robot['ip'], $robot['port'], $robot['pass']); //初始化
echo "[1]完成".PHP_EOL;
echo "[2]连接Spigot端...".PHP_EOL;
function connect_spigot() {
	do{
		$c = new yxmingy\ClientSocket();
		$conn = true;
		try{
			$c->connect("bgp.mcpe.club",5700);
			//$c->connect("localhost",2333);
		}catch(\Exception $e) {
			echo ("[2]连接失败,错误信息:".$e->getMessage().PHP_EOL);
			$conn = false;
		}
	} while (!$conn);
	echo "[2]连接成功".PHP_EOL;
	return $c;
}
$c = connect_spigot();
echo "[3]一切就绪，开始与服务端交互".PHP_EOL;
$c->setNonBlock();
$i = 0;
while(true) {
	while(($msg = $c->read()) === null ) {
		echo "[QaQ]与Spigot服务器连接断开，尝试重连...";
		$c = connect_spigot();
		sleep(10);
	}
	echo "$i";
	if(($i++)%10 == 0) {
		$c->write("cpdd\n");
		echo "[4]已发送心跳包".PHP_EOL;
	}
	$msg = trim($msg);
	sleep(1);
	if($msg == "dnmb") {
		echo "[4]心跳包交互成功".PHP_EOL;
	}
	if(preg_match('/GM/',$msg)) {
		foreach (explode("\n",$msg) as $m) {
			$m = explode(":",$m);
			QQROT::sengGroupMessage(GROUP_QQ_NUM,implode(array_slice($m,1)));
			echo "[OvO]新收到一条消息，已转发至QQ群".PHP_EOL;
		}
	}
}
$c->safeClose();