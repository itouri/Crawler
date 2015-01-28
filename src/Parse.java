import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import net.htmlparser.jericho.Source;


public class Parse {
	public static void parse(String urlStr,int loopCount) throws Exception{

		URL url = new URL(urlStr);
		Source html = new Source(url);

		Set<String> aList = new HashSet<>(); //リンク先のリスト
		Set<String> downloadList  = new HashSet<>(); //外部ファイルのリスト
		Set<String> changeList  = new HashSet<>(); //HTMLの変える部分リスト

		aList = Make.makeAList(urlStr);
		downloadList = Make.makeDownloadList(html);

		changeList.addAll(aList);
		changeList.addAll(downloadList);

		Save.saveDownload(downloadList);
		Save.saveHtml(urlStr,changeList);

		if(  loopCount < Main.loopNum){ //ループ数を見て再帰
			for(String a : aList){
				parse(a,loopCount+1);
			}
		}

	}
}
