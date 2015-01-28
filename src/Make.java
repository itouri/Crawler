import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;


public class Make {

	public static Set<String> makeAList(String urlStr) throws IOException{

		URL url = new URL(urlStr);
		Source html = new Source(url);

		Set<String> returnList = new HashSet<>();
		List<Element> tmpAList = html.getAllElements("a"); //リンク先をリストに格納

		for(Element a : tmpAList){

			String strA  = a.getAttributeValue("href");
			String checkA = null;

			if(strA == null || Char.checkHtml(strA)){	//nullが出てくる原因を潰したい　httpもhtmlもないならスキップ
			}else if(Char.checkHttp(strA)){ //httpがあるならそのまま
				checkA = strA;
			}else{
				checkA = Main.f_url + strA; //httpがないなら大元のurlを先頭につける
			}

			if(checkA != null && getResponseCode(new URL(checkA)) == 200){ //リンク先があるなら返すListにAdd
				returnList.add(checkA);
			}
		}
		return returnList;
	}

	public static Set<String> makeDownloadList(Source html) throws IOException{
		Set<String> returnList = new HashSet<>();

		List<Element> imgList = html.getAllElements("img");
		List<Element> linkList = html.getAllElements("link");
		List<Element> scriptList = html.getAllElements("script");

		//XMLを相手にするなら下記を記述
		html.fullSequentialParse();
		//Element body = html.getElementById("body");

		if(linkList != null){ //link出力（実質cssのみ）画像の時と殆ど変わらないから１つのメソッドで済むかも
			for (Element linkTag : linkList){
				String linkStr = linkTag.getAttributeValue("href");
				returnList.add(linkStr); //本当はMakeで判断したい→今はsaveDownload
			}
		}

		if(scriptList != null){ //scpirtの出力
			for (Element scriptTag : scriptList){
				String scriptSrcStr = scriptTag.getAttributeValue("src");
				if(scriptSrcStr != null && Char.checkSrash(scriptSrcStr)){//srcがない場合InputStreamでエラーになる
					returnList.add(scriptSrcStr);//本当はMakeで判断したい→今はsaveDownload
				}
			}
		}

		/*if(imgList != null){ //画像出力
			for (Element imgTag : imgList){
				String imgSrcStr = imgTag.getAttributeValue("src");
				returnList.add(imgSrcStr);
			}
		}*/

		return returnList;
	}

	/*private static Set<String> add(String str,Set<String> list){

	}*/

	public static int getResponseCode(URL u) throws IOException { //URLを確かめる
		HttpURLConnection huc =  (HttpURLConnection)  u.openConnection();
		huc.setRequestMethod("GET");
		huc.connect();
		return huc.getResponseCode();
	}
}
