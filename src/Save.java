import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;


public class Save {
	public static void saveDownload(Set<String> list) throws MalformedURLException, IOException{
		for(String tmpStr : list){
			String str;
			if(Char.checkHttp(tmpStr)){ //ローカル、グローバルの判断　本当はMake.addでやりたい
				str = tmpStr;
			}else{
				str = Main.f_url + tmpStr;
			}

			if(Make.getResponseCode(new URL(str)) == 200){ //正常なときだけ起動 本当はMakeでやりたい
				//! unkown protocol : android-app　httpがついてないとエラーになるのかな？

				InputStream inputFile = new URL(str).openStream();

				File saveFile = new File(Char.convertFileName(Char.makeDir(tmpStr)));
				saveFile.mkdirs(); //新しいファイルを生成

				System.out.println("DL" + tmpStr);
				OutputStream osFile = new FileOutputStream(Char.convertFileName(tmpStr));
				//! アクセスが拒否されました

				//画像書き込み
				byte[] buf = new byte[1024];
				int len = 0;

				while ((len = inputFile.read(buf)) > 0) {  //終わるまで書き込み
					osFile.write(buf, 0, len);
				}

				osFile.flush();
				osFile.close();
				inputFile.close();
			}
		}
	}

	public static void saveHtml(String urlStr,Set<String> changeList) throws Exception{
		URL url = new URL(urlStr);
		Source html = new Source(url);

		//XMLを相手にするなら下記を記述
		html.fullSequentialParse();
		//Element body = html.getElementById("body");
		List<Element> htmlList = html.getAllElements("html");

		File mkFile = null;
		File bwFile = null;

		if(urlStr.indexOf(Main.f_url) != -1){ //最初のurlを含んでいる場合 つまりローカル
			System.out.println(Main.f_url);
			System.out.println(urlStr);
			if(Main.f_url == urlStr){ //入力したアドレスならhome.htmlと名付ける
				bwFile = new File(Main.f_fileName + "home.html");
			}else{
				bwFile = new File(Main.f_fileName + urlStr.replaceFirst(Main.f_url,""));
				if((urlStr.replaceFirst(Main.f_url,"")).indexOf("/") != -1){
					mkFile = new File(Main.f_fileName + Char.makeDir(urlStr.replaceFirst(Main.f_url,"")));
				}else{
					mkFile = null;
				}
			}
		}else if(urlStr.lastIndexOf("/")+1 == urlStr.length()){//最後が"/"で終わる場合
			bwFile = new File(Char.convertFileName(urlStr.substring(0,urlStr.length()-1))); //
			mkFile = new File(Char.convertFileName(Char.makeDir(urlStr.substring(0,urlStr.length()-1))));
			System.out.println(Main.f_url);
			System.out.println(urlStr);
		}else{													//ソレ意外のフツーURLの場合
			bwFile = new File(Char.convertFileName(urlStr));
			mkFile = new File(Char.convertFileName(Char.makeDir(urlStr)));
			System.out.println(urlStr);
		}

		if(mkFile != null){
			mkFile.mkdirs();
		}

		String tmpHtml = null;
		if(htmlList != null){
			for (Element htmlTag : htmlList){ //HTMLのリンクをChar.convertFileNameする
				//System.out.println( htmlTag.getContent().toString());
				tmpHtml += htmlTag.getContent().toString();
				for(String change : changeList){
					tmpHtml = tmpHtml.replaceAll(change,Char.convertNotCrawlFileName(change));
				}
			}
			BufferedWriter bwHtml = new BufferedWriter(new FileWriter(bwFile));
			bwHtml.write(tmpHtml);
			bwHtml.close();
		}
	}
}
