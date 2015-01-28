import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Char {
	public static int getResponseCode(URL u) throws MalformedURLException, IOException {
		HttpURLConnection huc =  (HttpURLConnection)  u.openConnection();
		huc.setRequestMethod("GET");
		huc.connect();
		return huc.getResponseCode();
	}

	static public boolean checkHttp(String str){ //文字列にhttpが入ってないか判別 あるなら真
		if(str == null){
			return false;//なぜstrにnull?
		}
		if(str.indexOf(":") != -1 || str.indexOf("http") != -1 ) {  //見つかった場合
			return true;
		}else{
			return false;
		}
	}

	static public boolean checkSrash(String str){ //文字列に//,httpが入ってないか判別 あるなら偽
		if(str.indexOf("//") != -1 || str.indexOf("http") != -1 ){  //見つかった場合
			return true;
		}else{
			return false;
		}
	}

	static public boolean checkLastSrash(String str){ //文字列に//,httpが入ってないか判別 あるなら偽
		if(str.lastIndexOf("/") == str.length()-1 ){  //見つかった場合
			return true;
		}else{
			return false;
		}
	}

	static public boolean checkHtml(String str){
		if(str.indexOf("http") == -1 && str.indexOf("html") == -1){ //httpもhtmlもないならスキップ
			return true;
		}else{
			return false;
		}
	}

	static public String makeDir(String str){ //フォルダの名前のみ抽出 images/aaa.png → images
		int index;
		index = str.lastIndexOf("/"); //一番最後の"/"の場所を格納
		if(index != -1){
			str = str.substring(0,index); //0~indexの文字列を抽出
		}
		return str;
	}

	static public String convertFileName(String str){ //フォルダ名として使用できないものは削除
		if(str.indexOf("/") == 0){ //最初の１文字が/だった場合変換
			str.replaceFirst("/","");
		}

		if(str.lastIndexOf("/")+1 == str.length()){ //最後の１文字が/だった場合変換  最後がスラッシュ images→images/index.html
			//str.substring(0,str.length()-1);
			str = str + "index.html";
		}

		str = str.replaceAll("://","/");
		str = str.replaceAll("=","");
		str = str.replaceAll("<","");
		str = str.replaceAll(">","");
		str = str.replaceAll("|","");
		str = str.replaceAll("\\?","");
		str = str.replaceAll("\\*","");
		//str = str.replaceAll("*",""); なんかこれだけエラーがでる
		/*正規表現？ *,?など特別な文字をしているとエラー \\をつけよう*/

		return Main.f_fileName + str;
	}

	static public String convertNotCrawlFileName(String str){ //フォルダ名として使用できないものは削除
		if(str.indexOf("/") == 0){ //最初の１文字が/だった場合変換
			str.replaceFirst("/","");
		}

		if(str.lastIndexOf("/")+1 == str.length()){ //最後の１文字が/だった場合変換  最後がスラッシュ images→images/index.html
			//str.substring(0,str.length()-1);
			str = str + "index.html";
		}

		str = str.replaceAll("://","/");
		str = str.replaceAll("=","");
		str = str.replaceAll("<","");
		str = str.replaceAll(">","");
		str = str.replaceAll("|","");
		str = str.replaceAll("\\?","");
		str = str.replaceAll("\\*","");
		//str = str.replaceAll("*",""); なんかこれだけエラーがでる
		/*正規表現？ *,?など特別な文字をしているとエラー \\をつけよう*/

		return str;
	}
}
