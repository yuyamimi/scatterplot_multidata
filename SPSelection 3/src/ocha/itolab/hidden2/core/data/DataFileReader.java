package ocha.itolab.hidden2.core.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;


public class DataFileReader {
	BufferedReader breader;
	String filename;
	IndividualSet ps = null;
	int numdim = 0;
	String directory = "";
	int phase = 1, numExplain = 0, numObjective, numCategory = 0, numBoolean = 0, classId = -1;

	/**
	 * Plot�t�@�C�����J��
	 */
	void open(String filename) {
		this.filename = filename;
		numdim = 0;

		try {
			File file = new File(filename);
			breader = new BufferedReader(new FileReader(file));
			breader.ready();
			directory = file.getParent() + "/";
		} catch (Exception e) {
			System.err.println(e);
		}
	}


	/**
	 * Plot�t�@�C�������
	 */
	public void close() {
		try {
			breader.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}


	/**
	 * Plot�t�@�C����1�s��ǂ�
	 */
	public String[] readLine() {
		String wordarray[] = null;

		try {

			// EOF�܂œǂݑ�����
			String line = breader.readLine();
			if (line == null) return null;
			//System.out.println(line);

			// ��s�ł���Ζ��Ӗ��ȓ��e��Ԃ�
			if(line.length() <= 0) {
				wordarray = new String[1];
				wordarray[0] = "";
				return wordarray;
			}

			// 1�s��P�ꂲ�Ƃɋ�؂�
			StringTokenizer tokenBuffer = new StringTokenizer(line, ",");
			wordarray = new String[tokenBuffer.countTokens()];
			for(int i = 0; i < wordarray.length; i++) {
				wordarray[i] = tokenBuffer.nextToken();
			}

		} catch(Exception e) {
			e.printStackTrace();
		}

		// ��ǌ��ʂ�Ԃ�
		return wordarray;
	}




	/**
	 * �t�@�C����ǂ��IndividualSet�N���X��Ԃ�
	 */
	public IndividualSet read(String filename) {
		System.out.println("Reading " + filename + " ...");

		// �t�@�C�����J��
		open(filename);

		// �t�@�C����1�s���ǂ�
		while(true) {
			String w[] = readLine();
			if(w == null) break;
			if(w[0].length() <= 0) continue;

			// 3�s�ڈȍ~
			if(phase >= 3) {
				OneIndividual p = ps.addOneIndividual();
				int ie = 0, io = 0, ic = 0, ib = 0;

				for(int i = 0; i < w.length; i++) {
					if(ps.getValueType(i) == ps.TYPE_EXPLAIN) {
						p.explain[ie++] = Double.parseDouble(w[i]);
					}
					else if(ps.getValueType(i) == ps.TYPE_OBJECTIVE) {
						p.objective[io++] = Double.parseDouble(w[i]);
					}
					else if(ps.getValueType(i) == ps.TYPE_BOOLEAN) {
						p.bool[ib] = false;
						if(w[i].startsWith("true") == true) p.bool[ib] = true;
						if(w[i].startsWith("1") == true) p.bool[ib] = true;
						ib++;
					}
					else if(ps.getValueType(i) == ps.TYPE_CATEGORY) {
						p.category[ic++] = w[i];
					}
					else if(ps.getValueType(i) == ps.TYPE_NAME) {
						p.name = w[i];
					}
				}

			}

			// 2�s��
			else if(phase == 2) {

				// �e�ϐ��̖��O���Z�b�g����
				for(int i = 0; i < w.length; i++) {
					ps.setValueName(i, w[i]);
				}
				phase++;  continue;
			}

			// 1�s��
			else {

				// ���́E�o�͂̎��������m�肷��
				for(int i = 0; i < w.length; i++) {
					if(w[i].startsWith("Explain") == true)
						numExplain++;
					else if(w[i].startsWith("Objective") == true)
						numObjective++;
					else if(w[i].startsWith("Boolean") == true)
						numBoolean++;
					else if(w[i].startsWith("Category") == true)
						numCategory++;
				}
				System.out.println("Exp:" + numExplain);
				System.out.println("Obj:" + numObjective);

				ps = new IndividualSet(numExplain, numObjective, numCategory, numBoolean);
				for(int i = 0; i < w.length; i++) {
					if(w[i].startsWith("Explain") == true)
						ps.setValueType(i, ps.TYPE_EXPLAIN);
					else if(w[i].startsWith("Objective") == true)
						ps.setValueType(i, ps.TYPE_OBJECTIVE);
					else if(w[i].startsWith("Boolean") == true)
						ps.setValueType(i, ps.TYPE_BOOLEAN);
					else if(w[i].startsWith("Name") == true)
						ps.setValueType(i, ps.TYPE_NAME);
					else if(w[i].startsWith("Category") == true)
						ps.setValueType(i, ps.TYPE_CATEGORY);
				}


				phase++;  continue;
			}

		}


		// �t�@�C����ǂ񂾌�̌㏈��
		ps.filename = this.filename;
		ps.finalize();


		return ps;
	}


}
