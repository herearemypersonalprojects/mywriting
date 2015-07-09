passage getPassageNgram(ArrayList<qfield> question, transcript Transcript,
		int pos, int window_size, int window_step, int ngram) {
	passage p = new passage();
	ArrayList<String> name = new ArrayList<String>();

	p.window_pos1 = pos + window_size;
	p.window_pos2 = pos;

	if (pos < 0 || pos + window_size > Transcript.listwords.size())
		return p;

	boolean[] availTran = new boolean[window_size];
	for (int i = 0; i < window_size; availTran[i++] = true)
		;

	boolean[] availQues = new boolean[question.size()];
	for (int i = 0; i < question.size(); availQues[i++] = true)
		;

	// speaker
	for (int i = 0; i < question.size(); i++) {
		for (int j = pos; j < pos + window_size; j++)
			if (availTran[j - pos])
				if (question.get(i).stem
						.equals(Transcript.listwords.get(j).speaker)) {
					name.add(Transcript.listwords.get(j).speaker);
					availQues[i] = false;
					p.addCommonWord(Transcript.listwords.get(j).speaker,
							Transcript.listwords.get(j).pos, j,
							Score.speaker);
					if (p.window_pos1 > j)
						p.window_pos1 = j;
					if (p.window_pos2 < j)
						p.window_pos2 = j;

					p.score = p.score + Score.speaker;
					break;
				}
	}

	// stem
	for (int i = 0; i < question.size(); i++) {
		if (availQues[i])
			for (int j = pos; j < pos + window_size; j++)
				if (availTran[j - pos]) {
					boolean similarity = true;
					for (int ng = 0; ng < ngram
					&& j + ng < Transcript.listwords.size()
					&& i + ng < question.size(); ng++) {
						// if(Transcript.listwords.get(j+ng).stem.compareTo(
						// question.get(i+ng).stem)!=0)
						if (!question.get(i + ng).lemmas
								.contains(Transcript.listwords.get(j + ng).stem))
							// if(Transcript.listwords.get(j+ng).word.
							// compareTo(question.get(i+ng).word)!=0)
							similarity = false;
					}

					if (similarity) {
						availTran[j - pos] = false;
						availQues[i] = false;
						p.addCommonWord(Transcript.listwords.get(j).stem,
								Transcript.listwords.get(j).pos, j,
								Score.stem);
						if (p.window_pos1 > j)
							p.window_pos1 = j;
						if (p.window_pos2 < j)
							p.window_pos2 = j;

						p.score = p.score + Score.stem;

						if (name
								.contains(Transcript.listwords.get(j).speaker))
							p.score = p.score + Score.bonus;

						break;
					}
				}
	}

	// synonyms
	for (int i = 0; i < question.size(); i++)
		if (availQues[i]) {
			for (int j = pos; j < pos + window_size; j++)
				if (availTran[j - pos]) {
					boolean similarity = true;
					for (int ng = 0; ng < ngram
					&& j + ng < Transcript.listwords.size()
					&& i + ng < question.size(); ng++) {
						if (!Transcript.listwords.get(j + ng).synonyms
								.contains(question.get(i + ng).stem))
							similarity = false;
					}

					if (similarity) {
						availTran[j - pos] = false;
						p.addCommonWord(Transcript.listwords.get(j).stem,
								Transcript.listwords.get(j).pos, j,
								Score.synonym);
						if (p.window_pos1 > j)
							p.window_pos1 = j;
						if (p.window_pos2 < j)
							p.window_pos2 = j;

						p.score = p.score + Score.synonym;
						if (name
								.contains(Transcript.listwords.get(j).speaker))
							p.score = p.score + Score.bonus;

						break;
					}
				}
		}

	return p;
}
