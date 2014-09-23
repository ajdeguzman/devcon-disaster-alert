package com.devcon.devise;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MenuNewsFeed extends Fragment {
	private final String WEB_URL = "http://www.ajdeguzman.x10.mx/api/books.xml";
	private LoadTask task;
	String[] books = new String[] {"book_title", "book_published", "book_author", "book_genre"};
	int[] books_layout = new int[]{R.id.book_title, R.id.book_published, R.id.book_author, R.id.book_genre};
	private PullToRefreshListView lstBooks;
	private ProgressDialog pd; 
	String sample;
	String sample2;
	Document doc;
	DocumentBuilder db;
	DocumentBuilderFactory dbf;
	
	public MenuNewsFeed(){}
	private MenuItem menuItem;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_news, container, false);
     // Create a Card
        Card card = new Card(getActivity(), R.layout.row_card);
         
        // Create a CardHeader
        CardHeader header = new CardHeader(getActivity());
        header.setTitle("UCU holds Fun Run 2014");
                 
        card.setTitle("The quick brown fox");
        CardThumbnail thumb = new CardThumbnail(getActivity());
        thumb.setDrawableResource(R.drawable.ic_launcher);
                 
        card.addCardThumbnail(thumb);
                 
        // Add Header to card
        card.addCardHeader(header);
         
        // Set card in the cardView
        CardView cardView = (CardView) rootView.findViewById(R.id.carddemo);
        cardView.setCard(card);
		/*lstBooks = (PullToRefreshListView) rootView.findViewById(R.id.pull_to_refresh_listview);
		task = new LoadTask();
		lstBooks.setOnRefreshListener(new OnRefreshListener<ListView>() {
		    @Override
		    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		    	if(checkInternetConnection()){
					task.execute();
		    	}else{
					lstBooks.onRefreshComplete();
				   Toast.makeText(getActivity(), "Couldn't refresh feed", Toast.LENGTH_SHORT).show();
				}
		    }
		});
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(getActivity());
		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.sound_pull_down);
		soundListener.addSoundEvent(State.RESET, R.raw.sound_receive);
		soundListener.addSoundEvent(State.REFRESHING, R.raw.sound_refresh);
		lstBooks.setOnPullEventListener(soundListener);*/
        return rootView;
    }
	private boolean checkInternetConnection(){
		   boolean connected = false;
		      ConnectivityManager check = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		      if (check != null) 
		      {
		         NetworkInfo[] info = check.getAllNetworkInfo();
		         if (info != null) 
		            for (int i = 0; i <info.length; i++) 
		            if (info[i].getState() == NetworkInfo.State.CONNECTED)
		            {
		            	connected = true;
		            }
		      }
		      else{
		         Toast.makeText(getActivity(), "Couldn't refresh feed", Toast.LENGTH_SHORT).show();
		          }
		      return connected;
	}
	private class LoadTask extends AsyncTask<String,String,List>{
		@Override
		protected List doInBackground(String... arg0) {
			List<HashMap<String, String>> allBooks = new ArrayList<HashMap<String, String>>();
			try {
				dbf = DocumentBuilderFactory.newInstance();
				db = dbf.newDocumentBuilder();
				doc = db.parse(webConnect());
			} catch (Exception e) {
				e.printStackTrace();
			}
			NodeList nodeListBook = doc.getElementsByTagName("book");
			for(int i = 0; i < nodeListBook.getLength(); i++){
				HashMap<String, String> map = new HashMap<String, String>();
				Element elementBook = (Element) nodeListBook.item(i);
				
				NodeList nodeListTitle = elementBook.getElementsByTagName("title");	
				Element elementTitle = (Element) nodeListTitle.item(0);
				
				
				NodeList nodeListAuthor = elementBook.getElementsByTagName("author");	
				Element elementAuthor = (Element) nodeListAuthor.item(0);
				
				NodeList nodeListGenre = elementBook.getElementsByTagName("genre");
				Element elementGenre = (Element) nodeListGenre.item(0);
				
				NodeList nodeListDate= elementBook.getElementsByTagName("publish_date");
				Element elementPublish = (Element) nodeListDate.item(0);
				
				map.put("book_title", elementTitle.getFirstChild().getTextContent());
				map.put("book_author", elementAuthor.getFirstChild().getTextContent());
				map.put("book_genre", elementGenre.getFirstChild().getTextContent());
				map.put("book_published", elementPublish.getFirstChild().getTextContent());
				allBooks.add(map);
				
			}
			return allBooks;
		}
		@Override
		protected void onPostExecute(List str){
			SimpleAdapter simple = new SimpleAdapter(getActivity(), str, R.layout.books_layout, books, books_layout);
			lstBooks.setAdapter(simple);
			simple.notifyDataSetChanged();
			
			lstBooks.onRefreshComplete();
			super.onPostExecute(str);
		}
	}
	private InputStream webConnect(){
		InputStream in = null;
		try {
			URL url = new URL(WEB_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			in = conn.getInputStream();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}
	
		
}
