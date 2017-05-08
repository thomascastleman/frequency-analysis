import java.util.*;
import java.io.*;

class Text {

	// words mapped to frequency
	public static HashMap<ArrayList<String>, Double> words = new HashMap<ArrayList<String>, Double>();

	// list of insignificant words to ignore if looking at individual words, not groups
	public static ArrayList<String> insignificant = new ArrayList<String>(Arrays.asList("the", "be", "to", "of", "and", "a", "an", "in", "that", "have", "I", "it", "not", "with", "for"));

	// total number of words in input
	public static double total = 0.0;

	public static void main(String[] args) {
		// lines in input file
		ArrayList<String> lines = new ArrayList<String>();

		// get lines of text
        File f = new File("C:\\Users\\T410\\Desktop\\Text Analysis\\Text\\formatted.txt");
        Scanner scan = null;
  
        try {
            scan = new Scanner(f);
            while (scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        analyze(lines, 3, 100);
	}

	// analyze an array of lines of text, using (order) number of words in a group, and displaying (amount) number of group results
	private static void analyze(ArrayList<String> lines, int order, int amount) {
		// get frequencies of every word
		for (int i = 0; i < lines.size(); i++) {
			sum(lines.get(i), order);
		}

		System.out.println("\n" + (int) total + " collections of " + order + " words.\n");

		// get keys from words hashmap
		List<ArrayList<String>> keys = new ArrayList<ArrayList<String>>(words.keySet());

		// convert all frequencies to percentages
		for (int i = 0; i < keys.size(); i++) {
			ArrayList<String> collection = keys.get(i);
			words.put(collection, (words.get(collection) / total) * 100.0);
		}

		ArrayList<ArrayList<String>> collectionsInOrder = new ArrayList<ArrayList<String>>();
		ArrayList<Double> frequencies = new ArrayList<Double>();


		// get the top "n" words
		for (int i = 0; i < (amount > total ? total : amount); i++) {

			// initialize maximum as first index
			ArrayList<String> max = keys.get(0);

			// get key of highest remaining frequency in words
			for (int key = 0; key < keys.size(); key++) {
				if (words.get(keys.get(key)) > words.get(max)) {
					max = keys.get(key);
				}
			}

			if (words.get(max) == 0.0) {
				break;
			}

			if (!(max.size() == 1 && insignificant.contains(max.get(0).toLowerCase()))) {
				// add word to ordered list, add frequency
		 		collectionsInOrder.add(max);
				frequencies.add(words.get(max));
			}

			// make frequency 0, so next maximum will be found on next pass
			words.put(max, 0.0);
		}

		System.out.println("Frequencies (top " + collectionsInOrder.size() + " results): ");

		// display top frequencies
		for (int i = 0; i < collectionsInOrder.size(); i++) {
			int count = (int) Math.floor((frequencies.get(i) / 100) * total);

			// show frequency and occurence
			System.out.printf("%.3f%%\t(%d)\t", frequencies.get(i), count);

			// print phrase
			System.out.printf("\"");
			for (int j = 0; j < collectionsInOrder.get(i).size(); j++) {
				System.out.printf("%s", collectionsInOrder.get(i).get(j));
				if (j != collectionsInOrder.get(i).size() - 1) {
					System.out.print(" ");
				}
			}
			System.out.printf("\"\n");
		}
	}

	// parse a line of text, update words, and calculate total
	private static void sum(String line, int order) {
		// transfer words into ArrayList
		ArrayList<String> w = new ArrayList<String>();
		String[] temp = line.split(" ");
		for (int i = 0; i < temp.length; i++) {
			w.add(temp[i]);
		}

		// for word in line
		for (int i = 0; i < w.size() - order; i++) {
			// get group of order length
			ArrayList<String> sub = new ArrayList<String>(w.subList(i, i + order));

			// get rid of ending punctuation
			for (int j = 0; j < sub.size(); j++) {
				String last = Character.toString(sub.get(j).charAt(sub.get(j).length() - 1));
				String str = sub.get(j).substring(0, sub.get(j).length() - 1);
				sub.set(j, str + last.replaceAll("\\W", ""));
			}

			// if no hashmap value yet
			if (words.get(sub) == null) {
				// add to words
				words.put(sub, 0.0);
			}

			// increment frequency
			words.put(sub, words.get(sub) + 1.0);
			total += 1.0;
		}
	}
}