package org.googlecode.vkontakte_android.download;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Alexander
 * Date: 06.09.2009
 * Time: 18:26:18
 * To change this template use File | Settings | File Templates.
 */
public class ContactsDownloader {

    public ContactsDownloader() {
    }

    public List<VkRecord> download() throws IOException {

        //TODO repair
        final AbstractHttpClient client = null; //CGuiTest.api.getHttpClient();

        List<ContactsDownloader.VkRecord> records = new ArrayList<ContactsDownloader.VkRecord>(600);

		if (client != null) {

			for (int page = 1; page < 100; page++) {
				final HttpGet get = new HttpGet(
						"http://pda.vkontakte.ru/friends" + page);
				final HttpResponse response = client.execute(get);

				final List<VkRecord> chunk = parse(new InputStreamReader(
						response.getEntity().getContent(), "UTF-8"));
				if (chunk.isEmpty())
					break;
				records.addAll(chunk);
			}
		}

        return records;
    }

    public List<VkRecord> parse(Reader in) throws IOException {
        final BufferedReader rd = new BufferedReader(in);

        List<ContactsDownloader.VkRecord> records = new ArrayList<ContactsDownloader.VkRecord>(20);
        ContactsDownloader.VkRecord curr = null;

        final Pattern idAndName = Pattern.compile("<a href=./id(\\d+).>(.+?)</a>");
        final Pattern phone = Pattern.compile("<a href=.tel:(.+?).>");

        String line;
        while ((line = rd.readLine()) != null) {
            if (!line.contains("href")) continue;

            final Matcher nameMatcher = idAndName.matcher(line);
            if (nameMatcher.find()) {
                curr = new ContactsDownloader.VkRecord();
                records.add(curr);
                curr.vkId = Long.parseLong(nameMatcher.group(1));
                curr.name = nameMatcher.group(2);
            }

            if (curr == null) continue;
            final Matcher telMatcher = phone.matcher(line);
            while (telMatcher.find()) {
                curr.phones.add(telMatcher.group(1));
            }
        }

        return records;
    }

    public static class VkRecord {
        public long vkId;
        public String name;
        public List<String> phones = new ArrayList<String>(2);

        @Override
        public String toString() {
            return name + " (" + vkId + ") " + (phones.isEmpty() ? "" : phones.toString());
        }
    }
}
