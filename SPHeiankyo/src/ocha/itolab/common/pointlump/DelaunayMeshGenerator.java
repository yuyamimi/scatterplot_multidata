package ocha.itolab.common.pointlump;

import java.util.ArrayList;
import java.util.Vector;

/**
 * ���b�V���𐶐�����
 */
public class DelaunayMeshGenerator {
	static ArrayList swapStack;
	static Mesh mesh;
	
	/**
	 * ���b�V���𐶐�����
	 */
	public static void generate(Mesh m) {
		mesh = m;
		swapStack = new ArrayList();
		m.triangles.clear();
	
		// �S�̂��͂ޑ傫�Ȏl�p�`�𐶐�����
		generateSuperRectangle();
		
		// ���_��1���������ADelaunay�O�p���b�V�������A���S���Y����K�p����
		int numv = mesh.getNumVertices();
		for(int i = 0; i < numv; i++) {
			/*
			if(i % 10000 == 0)
				System.out.println("    Delaunay " + i  + "/" + numv);
			*/
			Vertex vertex = mesh.getVertex(i);
			placeOneVertexByDelaunay(vertex);
	    }
		
    	// �傫�Ȏl�p�`���\�����钸�_�A����т�������L����O�p�`���A�폜����
    	// ���̑��A���Ƃ̐}�`�̊O���ɂ���O�p�`���폜����
    	removeOuterTriangle();
    
    	/*
    	for(int i = 0; i < mesh.getNumTriangles(); i++) {
    		Triangle t = mesh.getTriangle(i);
    		Vertex v[] = t.vertices;
    		System.out.println("Delaunay " + i + 
    				": (" + v[0].id + ":" + v[0].getPosition()[0] + "," + v[0].getPosition()[1] + 
    				") (" + v[1].id + ":" + v[1].getPosition()[0] + "," + v[1].getPosition()[1] + 
    				") (" + v[2].id + ":" + v[2].getPosition()[0] + "," + v[2].getPosition()[1] + ")");
    	}
    	*/
	}



	/**
	 * ���_1���������ADelaunay�O�p���b�V�������A���S���Y����K�p����
	 * @param hall
	 */
	static void placeOneVertexByDelaunay(Vertex vertex) {

		Triangle triangle;
		int  ret;

		// ���_������O�p�`��������
		triangle = findTriangleEncloseCurrentVertex(vertex);
		if( triangle == null ) {
			System.out.println("   ??? findTriangleEncloseCurrentVertex Triangle is NULL");
			return;
		}

		// �O�p�`��3�Ɋ���
		divideTriangle(vertex, triangle);

		// �O�p�`�̕ӑg�݊������ċA�I�Ɏ��s����
		recursiveSwap();

	}
	
 
	/**
	 * �畆�̈���͂ޑ傫�Ȏl�p�`�̈�𐶐�����
	 */
    static void generateSuperRectangle() {

    	// 4���_�̍��W�l���Z�o����
    	double minx = 1.0e+30, miny = 1.0e+30, maxx = -1.0e+30, maxy = -1.0e+30;
    	for(int i = 0; i < mesh.getNumVertices(); i++) {
    		Vertex v = mesh.getVertex(i);
    		double pos[] = v.getPosition();
    		if(minx > pos[0]) minx = pos[0];
    		if(miny > pos[1]) miny = pos[1];
    		if(maxx < pos[0]) maxx = pos[0];
    		if(maxy < pos[1]) maxy = pos[1];
    	}
    	double x1 = minx - 3.0 * (maxx - minx);
    	double x2 = maxx + 3.0 * (maxx - minx);
    	double y1 = miny - 3.0 * (maxy - miny);
    	double y2 = maxy + 3.0 * (maxy - miny);
    	
    	// 2�̑傫�ȎO�p�`��4���_�𐶐�����
    	Vertex dv1 = mesh.addOneVertex();
    	Vertex dv2 = mesh.addOneVertex();
    	Vertex dv3 = mesh.addOneVertex();
    	Vertex dv4 = mesh.addOneVertex();
        	
    	dv1.setPosition(x1, y1, 0.0);
    	dv2.setPosition(x2, y1, 0.0);
    	dv3.setPosition(x2, y2, 0.0);
    	dv4.setPosition(x1, y2, 0.0);
    	
    	// 2�̑傫�ȎO�p�`�𐶐�����
    	Triangle t1 = mesh.addOneTriangle();
    	Triangle t2 = mesh.addOneTriangle();

    	// 2�̎O�p�`�̗אڊ֌W���\�z����
    	t1.setVertices(dv1, dv2, dv3);
		t2.setVertices(dv3, dv4, dv1);
		t1.setAdjacents(null, null, t2);
		t2.setAdjacents(null, null, t1);
				
	}

    
    /**
     * �傫�Ȏl�p�`���\�����钸�_�A����т�������L����O�p�`���A�폜����
     * ���̑��A�􉽓I�ɐ}�`�̊O���ɂ���O�p�`���A�폜����
     */
    static void removeOuterTriangle() {
    	int numv = mesh.getNumVertices() - 4;
    	double tcenter[] = new double[3];
    	
    	// �e�X�̎O�p�`�ɂ���
    	for(int i = 0; i < mesh.getNumTriangles(); i++) {
    		Triangle t = mesh.getTriangle(i);
    		Vertex v[] = t.getVertices();
    		
    		// �̈���͂ޑ傫�Ȏl�p�`�ɐڂ��Ă���Ȃ�A���̎O�p�`���폜����
    		boolean shouldRemove = false;
    		for(int j = 0; j < 3; j++) {
    			if(v[j].getId() >= numv) {
    				shouldRemove = true;  break;
    			}
    		}
    		if(shouldRemove == true) {
    			mesh.removeOneTriangle(t);
    			i--;  continue;
    		}
    		
    	}
    	
    	// �傫�Ȏl�p�`���\�����钸�_���폜����
    	for(int i = numv; i < mesh.getNumVertices();) {
    		Vertex v = mesh.getVertex(i);
    		mesh.removeOneVertex(v);
    	}
    	
    }

    
    
    /**
     * �^����ꂽ1�_���}�`�̊O���ɂ��邩�𔻒肷��
     */
    /*
    static boolean isOuterPosition(double p[]) {
    	double svpos[][] = skinset.getSkinRegion();
    	int sp = 0, sn = 0;
    	
    	// ���Ƃ̐}�`�̊e�ӂɂ���
    	for(int i = 0; i < svpos.length; i++) {
    		int i1 = (i == 0) ? (svpos.length - 1) : (i - 1);
    		double xx = 0.0;
    		
    		// ���Y�ӂ�x���ɕ��s���A���Y�_�Ɠ���y���W�l��ʉ߂��Ȃ��ꍇ�́A�ΏۊO�Ƃ���
    		if(Math.abs(svpos[i][1] - svpos[i1][1]) < 1.0e-6) 
    			continue;
    		if(svpos[i][1] > p[1] && svpos[i1][1] > p[1]) 
    			continue;
    		if(svpos[i][1] < p[1] && svpos[i1][1] < p[1]) 
    			continue;
    		
    		// ���Y�ӂ�y���ɕ��s�ł����
    		if(Math.abs(svpos[i][0] - svpos[i1][0]) < 1.0e-6) {
    			xx = svpos[i][0];
    		}
    		
    		// ���Y�ӂ�x���ɂ�y���ɂ����s�łȂ����
    		// ���Y�_�Ɠ���y���W�l�ɂ����铖�Y�ӏ��x���W�l�����߂�
    		else {
    			double aa = (svpos[i][0] - svpos[i1][0]) / (svpos[i][1] - svpos[i1][1]);
    			double bb = svpos[i][0] - aa * svpos[i][1];
    			xx = aa * p[1] + bb;
    		}
    		
    		// ���Y�_��xx��荶�ɂ��邩�E�ɂ��邩��sp�܂���sn��1��������
    		if(p[0] > xx) sp++;
    		else          sn++;
    	}
    	
    	// sp��sn����ł���΁A���Y�}�`�̓����ɂ���
    	if((sp % 2 == 1) && (sn % 2 == 1)) return false;
    	
    	return true;
    }
    */

    
    /**
     * ���ݏ������̒��_���͂ގO�p�`����肷��
     */
    static Triangle findTriangleEncloseCurrentVertex(Vertex vertex) {
	
    	Triangle triangle = mesh.getTriangle(0);
    	int   ret;
	

    	//  1��ڂ̒���: �אڐ��ɗ����������Ȍ��� 
    	for(int i = 0; i < mesh.getNumTriangles(); i++) {
    		if(triangle == null ) break;
    		ret = checkTriangleEncloseCurrentVertex(vertex, triangle);
    		if( ret < 0 ) return triangle;
    		Triangle adjacents[] = triangle.getAdjacents();
    		triangle = adjacents[ret];
    	} 
	
    	//  2��ڂ̒���: Straightforward Search
    	for(int i = 0; i < mesh.getNumTriangles(); i++) { 
    		triangle = mesh.getTriangle(i);
    		ret = checkTriangleEncloseCurrentVertex(vertex, triangle);
    		if( ret < 0 ) return triangle;
    	}
	
    	// �S���O�p�`��������Ȃ����null��Ԃ�
    	return null;
    }
    
	/**
	 * �O�p�`�����_���͂�ł��邩�ۂ����`�F�b�N����
	 */
    static int checkTriangleEncloseCurrentVertex(Vertex v, Triangle t) {
    	Vertex vertices[] = t.getVertices();
    	double pos0[] = v.getPosition();
    	
    	// ��u�̊e�ӂɂ���
    	for (int i = 0; i < 3; i++) {	
    		int i1 = (i == 2) ? 0 : (i + 1);	
    		double pos1[] = vertices[i].getPosition();
    		double pos2[] = vertices[i1].getPosition();
    		
    		double a = (pos1[1] - pos0[1]) * (pos2[0] - pos0[0]);
    		double b = (pos1[0] - pos0[0]) * (pos2[1] - pos0[1]);
	 
    		// ���ꂪ��������Ȃ�Ai�Ԗڂ̗אڔ�u��T�����ׂ�
    		if( a - b > 0 ) return i;
    	}

    	// ���ꂪ��������Ȃ�A��u���ь����͂�ł���
    	return -1;
    }
    
    
    /**
     * �V�����ь��Ŕ�u��3��������
     */
    static void divideTriangle(Vertex v, Triangle t) {
	
	    Triangle  new0, new1, new2; 
	    Triangle  adjacents[] = t.getAdjacents();
	    Triangle  adj0 = adjacents[0];
	    Triangle  adj1 = adjacents[1];
	    Triangle  adj2 = adjacents[2];
    	Vertex  vertices[] = t.getVertices();
    	Vertex  v0 = vertices[0];
    	Vertex  v1 = vertices[1];
    	Vertex  v2 = vertices[2];

    	// �V������u���m�ۂ���
    	new0 = t;
    	new1 = mesh.addOneTriangle();
    	new2 = mesh.addOneTriangle();
	
    	new0.setVertices(v, v0, v1); 
    	new0.setAdjacents(new2, adj0, new1);
	
    	new1.setVertices(v, v1, v2); 
    	new1.setAdjacents(new0, adj1, new2);
	
    	new2.setVertices(v, v2, v0); 
    	new2.setAdjacents(new1, adj2, new0);
    	
    	// �V������u���X�^�b�N����Ɠ����ɁA�אڊ֌W���X�V����
    	if( adj0 != null ) {
    		//replaceAdjacency(adj0, Triangle, new0);
    		pushTriangleToStack(new0);
    	}
    	if( adj1 != null ) {
    		replaceAdjacency(adj1, t, new1);
    		pushTriangleToStack(new1);
    	}
    	if( adj2 != null ) {
    		replaceAdjacency(adj2, t, new2);
    		pushTriangleToStack(new2);
    	}
    }
    
    
	/**
	 * ��u�̗אڊ֌W���X�V����
	 */
	static void replaceAdjacency(Triangle t, Triangle oldt, Triangle newt) {
		Triangle adjacents[] = t.getAdjacents();
		
		for( int i = 0; i < 3; i++ ) {
			if(adjacents[i] == oldt) {
				adjacents[i] = newt;
				t.setAdjacents(adjacents[0], adjacents[1], adjacents[2]);
				return; 
			}
		}
	} 


	/**
	 * �X�^�b�N�ɔ�u��o�^����
	 */
	static void pushTriangleToStack(Triangle t) { 
		swapStack.add(t);
	} 

	/**
	 * �X�^�b�N�����u�𒊏o����
	 */
	static Triangle popTriangleFromStack( ) {
		if( swapStack.size() <= 0 ) return null;
		Triangle ret = (Triangle)swapStack.get(swapStack.size() - 1);
		swapStack.remove((Object)ret);
		return ret;
	} 


	/**
	 * ��u�̕ӂ̑g�݊������ċA�I�ɔ�������
	 */
	static void recursiveSwap( ) {

		Triangle ltri, rtri, adj1, adj2, adj3, adj4;
		Vertex   snod1, snod2, lnod, rnod;
		int      i;
		boolean  ret;

		//
		// Repeat until no triangles are needed to swap edges
		//
		while( true ) {

			//
			//  Extract a triangle from a stack
			//
			ltri = popTriangleFromStack();
			if( ltri == null ) break;

			//
			//  Check the shared edge
			//     of the adjacent two triangle,
			//     "ltri" and "rtri", should be swapped
			//
			//                  * snod1
			//         adj3   / | \     adj2
			//              /   |   \
			//            /     |     \
			//     lnod *  ltri | rtri  * rnod
			//            \     |     /
			//              \   |   /
			//         adj4   \ | /     adj1
			//                  * snod2
			//
			Triangle ladjacents[] = ltri.getAdjacents();
			Triangle la0 = ladjacents[0];
			Triangle la1 = ladjacents[1];
			Triangle la2 = ladjacents[2];
			Vertex lvertices[] = ltri.getVertices();
			Vertex lv0 = lvertices[0];
			Vertex lv1 = lvertices[1];
			Vertex lv2 = lvertices[2];
			rtri = la1;
			if(rtri == null) {
				System.out.println("   ??? RecursiveSwap Adjacent Triangle is NULL");
				continue;
			}
			lnod = lv0;
			adj3 = la2;
			adj4 = la0;
			Triangle radjacents[] = rtri.getAdjacents();
			Vertex rvertices[] = rtri.getVertices();
			for(i = 0; i < 3; i++) {
				if(radjacents[i] == ltri)
					break;
			}
			if(i == 3) continue;
			snod1 = rvertices[i];
			i = (i == 2) ? 0 : (i + 1);
			snod2 = rvertices[i];
			adj1 = radjacents[i];
			i = (i == 2) ? 0 : (i + 1);
			rnod  = rvertices[i];
			adj2 = radjacents[i];

			//System.out.println("   swap: snod1=" + snod1.getId() + " snod2=" + snod2.getId() + " lnod=" + lnod.getId() + " rnod=" + rnod.getId());
			
			//
			//  Check if the triangles may not be swapped
			//
			//System.out.println("     swap snod1=" + snod1.getId() + " snod2=" + snod2.getId() + " lnod=" + lnod.getId() + " rnod=" + rnod.getId());
			if( (adj1 == adj4) || (adj2 == adj3) ) {
				continue;
			}
			ret = checkTriangleOverwrap( lnod, rnod, snod1, snod2 );
			//if(ret == true) System.out.println("       ret1=" + ret);
			if(ret == true) continue;

			//
			//  Check if the triangles are
			//    geometrically better to be swapped
			//	
			ret = checkShouldSwap( lnod, rnod, snod1, snod2 );
			//if(ret == true) System.out.println("       ret2=" + ret);
			if(ret == false) continue;

			//
			//  Swap the shared edge and
			//	 reset two triangles
			//
			ltri.setVertices(lnod, snod2, rnod);
			ltri.setAdjacents(adj4, adj1, rtri);
			
			rtri.setVertices(lnod, rnod, snod1);
			rtri.setAdjacents(ltri, adj2, adj3);
			
			if( adj1 != null ) {
				replaceAdjacency(adj1, rtri, ltri);
			}
			if( adj3 != null ) {
				replaceAdjacency(adj3, ltri, rtri);
			}

			//
			//  Register the swapped triangles ino a stack
			//
			if( adj1 != null ) {
				pushTriangleToStack( ltri );
			}
			if( adj2 != null ) {
				pushTriangleToStack( rtri );
			}	
	    
		} 
	}


	/**
	 * ��u�Ԃ̏d�Ȃ�𔻒肷��
	 */
	static boolean checkTriangleOverwrap(
		Vertex lnod, Vertex rnod, Vertex snod1, Vertex snod2  ) {

		double	ulr, vlr, ans1, ans2;
		double lpos[] = lnod.getPosition();
		double rpos[] = rnod.getPosition();
		double s1pos[] = snod1.getPosition();
		double s2pos[] = snod2.getPosition();

		ulr = lpos[0] - rpos[0];
		vlr = lpos[1] - rpos[1];
		ans1 = (s1pos[0] - lpos[0]) * vlr - 
		       (s1pos[1] - lpos[1]) * ulr;
		ans2 = (s2pos[0] - lpos[0]) * vlr - 
		       (s2pos[1] - lpos[1]) * ulr;

		if( ans1 * ans2 >= 0.0 ) return true;
		else                     return false;

	}


	/**
	 * 2�̔�u�Ԃ̕ӂ�g�ݑւ���ׂ����ۂ��A�􉽓I�ɔ��肷��
	 */
	static boolean checkShouldSwap( 
		Vertex lnod, Vertex rnod, Vertex snod1, Vertex snod2  ) {

		double    s1l[] = new double[2];
		double    s1r[] = new double[2];
		double    s2l[] = new double[2];
		double    s2r[] = new double[2];
		double    cosl, cosr, sinl, sinr, sinlr;
		double lpos[] = lnod.getPosition();
		double rpos[] = rnod.getPosition();
		double s1pos[] = snod1.getPosition();
		double s2pos[] = snod2.getPosition();
		
		s1l[0] = s1pos[0] - lpos[0];	
		s1l[1] = s1pos[1] - lpos[1];	
		s2l[0] = s2pos[0] - lpos[0];	
		s2l[1] = s2pos[1] - lpos[1];	
		s1r[0] = s1pos[0] - rpos[0];	
		s1r[1] = s1pos[1] - rpos[1];	
		s2r[0] = s2pos[0] - rpos[0];	
		s2r[1] = s2pos[1] - rpos[1];	

		// �א�2�ӂ̂Ȃ��p��180�x�ɋ߂��ꍇ
		if(isParallel(s1l, s2l) == true) return true;
		if(isParallel(s1r, s2r) == true) return true;
		if(isParallel(s1l, s1r) == true) return false;
		if(isParallel(s2l, s2r) == true) return false;
		
		// �����������Ƃ��݊p�܂��͗����Ƃ��s�p�̏ꍇ
		cosl = s1l[0] * s2l[0] + s1l[1] * s2l[1];
		cosr = s1r[0] * s2r[0] + s1r[1] * s2r[1];
		if((cosl > 0.0) && (cosr > 0.0)) return false;
		if((cosl < 0.0) && (cosr < 0.0)) return true;

		// �����̊p�x�̘a�����@�藝�ŕ]������
		sinl = s2l[0] * s1l[1] - s1l[0] * s2l[1];
		sinr = s1r[0] * s2r[1] - s2r[0] * s1r[1];
		sinlr = sinl * cosr + sinr * cosl;
		if (sinlr < 0.0) return true;
		else             return false;

	}


	/**
	 * 2�̃x�N�g�������s����]������
	 */
	static boolean isParallel(double v1[], double v2[]) {

		// �ǂ����Y���ɕ��s
		if(Math.abs(v1[0]) < 1.0e-8 && Math.abs(v2[0]) < 1.0e-8)
			return true;
		
		// �ǂ����X���ɕ��s
		if(Math.abs(v1[1]) < 1.0e-8 && Math.abs(v2[1]) < 1.0e-8)
			return true;
		
		// �X��������
		if(Math.abs(Math.abs(v1[1] / v1[0]) - Math.abs(v2[1] / v2[0])) < 1.0e-8)
			return true;
		
		return false;
	}
	
}
