package com.thoughtworks.mingle.murmurs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ocpsoft.pretty.time.PrettyTime;

public class Murmur {
//	murmurs resource xml - attributes
//	id: Integer; read only, the system generated id for a murmur. This id is globally unique.
//	author: Resource; details of the user who has created a murmur. If the murmur was posted using Jabber, this will be only available if the user has entered their Jabber user name on their Mingle profile page.
//	body: String; the content of a murmur
//	created_at: Datetime; read only, date and time of a murmur creation.
//	jabber_user_name: String; if the murmur was posted using Jabber, this is the name of the Jabber user who created the murmur.
//	is_truncated: Boolean; indicates if the murmur body is truncated. If the body is truncated, you will need to request that specific murmur to see the full murmur body.
//	stream: XML Element; read only, Where the murmur came from. The type attribute will be either, "comment" or "default". If stream type is "comment", additional information about where the comment was made will be included.

	private static final PrettyTime prettyTime = new PrettyTime();
	
	public static List<Map<String, String>> MURMURS_DATA = new ArrayList<Map<String, String>>();
	private static List<Murmur> TEST_MURMURS = new ArrayList<Murmur>();
	static {
		Calendar cal = Calendar.getInstance();
		
		TEST_MURMURS.add(new Murmur(10, "odessa", "I am tall and skittish", cal.getTime(), "", false));
		cal.add(Calendar.MINUTE, -5);
		TEST_MURMURS.add(new Murmur(9, "suds", "Yum rawhide bits", cal.getTime(), "", false));
		cal.add(Calendar.HOUR, -1);
		TEST_MURMURS.add(new Murmur(8, "elmer", "Bumble bumble bumble.  I failed out of puppy school", cal.getTime(), "", false));
		cal.add(Calendar.HOUR, -5);
		TEST_MURMURS.add(new Murmur(7, "bruno", "", cal.getTime(), "", false));
		cal.add(Calendar.DATE, -1);
		TEST_MURMURS.add(new Murmur(6, "rayo", "Grrrr I will kill you!", cal.getTime(), "", false));
		cal.add(Calendar.DATE, -8);
		TEST_MURMURS.add(new Murmur(5, "osito", "Prancing around town", cal.getTime(), "", false));
		cal.add(Calendar.MONTH, -2);
		TEST_MURMURS.add(new Murmur(4, "oscuro", "*psssssss*", cal.getTime(), "", false));
		cal.add(Calendar.MONTH, -3);
		TEST_MURMURS.add(new Murmur(3, "mani", "I see dead people", cal.getTime(), "", false));
		cal.add(Calendar.YEAR, -1);
		TEST_MURMURS.add(new Murmur(2, "zorro", "When's lunch?", cal.getTime(), "", false));
		cal.add(Calendar.YEAR, -3);
		TEST_MURMURS.add(new Murmur(1, "bella", "Around and around I go.  Where I stop nobody knows", cal.getTime(), "", false));

		for (Murmur m : TEST_MURMURS) {
			MURMURS_DATA.add(m.toMap());
		}
	}
	
	private final int id;
	private final String author;
	private final String body;
	private final Date createdAt;
	private final String stream;
	private final boolean truncated;
	
	public Murmur(int id, String author, String body, Date createdAt,
			String stream, boolean truncated) {
		this.id = id;
		this.author = author;
		this.body = body;
		this.createdAt = createdAt;
		this.stream = stream;
		this.truncated = truncated;
	}
	
	public int getId() {
		return id;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getShortBody() {
		if (body.length() > 32) {
			return body.substring(0, 32);
		}
		return body;
	}
	
	public String getBody() {
		return body;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public String getStream() {
		return stream;
	}
	
	public String toString() {
		return String.format("%s: %s", this.author, this.body);
	}
	
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("AUTHOR", this.author);
		map.put("BODY", this.body);
		map.put("CREATED_AT", prettyTime.format(this.createdAt));
		return map;
	}
}
