package core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import application.MainController;

public class StoreDAO {

	private ResultSet rs;
	private String sql;
	private ArrayList<Store> list;

	public StoreDAO() {}

	/*요식업소_식사조회(전체)*/ 
	public ArrayList<Store> selectAllMeal() throws SQLException {
		
		rs = null; list = null;
		Mysql mysql = Mysql.getConnection();	//호출	
		sql = "select * from 요식업소 where 상권업종중분류코드 IN ('q01','q02','q03','q04','q05','q06','q07','q10','q13','q14')";
		mysql.sql(sql);
		rs = mysql.select();
		
		list = new ArrayList<Store>();
		while(rs.next()) {
			Integer storeNumber = rs.getInt("상가업소번호");
			String storeName = rs.getString("상호명");
			String storeLocation = rs.getString("지점명");
			String codeMedium = rs.getString("상권업종중분류코드");
			String codeSmall = rs.getString("상권업종소분류코드");
			String storeAddress = rs.getString("도로명주소");
			double kDegree = rs.getDouble("경도");
			double wDegree = rs.getDouble("위도");
			Store s = new Store(storeNumber,storeName,storeLocation,codeMedium,codeSmall,storeAddress,kDegree,wDegree);
			list.add(s);
		}		
		return list;
	}
	
	
	/*요식업소_디저트조회(전체) -> 전체 불러와서 뿌려줄때 깔끔하게 , 이후에 ui 상황 보고 요구하는것 일부만 들고오기로 수정하기. 아니면 과부하 씨게 걸림*/
	public ArrayList<Store> selectAllDessert() throws SQLException {
		
		rs = null; list = null;
		Mysql mysql = Mysql.getConnection();	//호출	
		sql = "select * from 요식업소 where 상권업종중분류코드 IN ('q08','q12')";
		mysql.sql(sql);
		rs = mysql.select();	

		list = new ArrayList<Store>();
		while(rs.next()) {
			Integer storeNumber = rs.getInt("상가업소번호");
			String storeName = rs.getString("상호명");
			String storeLocation = rs.getString("지점명");
			String codeMedium = rs.getString("상권업종중분류코드");
			String codeSmall = rs.getString("상권업종소분류코드");
			String storeAddress = rs.getString("도로명주소");
			double kDegree = rs.getDouble("경도");
			double wDegree = rs.getDouble("위도");
			Store s = new Store(storeNumber,storeName,storeLocation,codeMedium,codeSmall,storeAddress,kDegree,wDegree);
			list.add(s);
		}
		return list;
	}
	
	
	/*요식업소_주점조회(전체)*/ 
	public ArrayList<Store> selectAllBeer() throws SQLException {
		
		rs = null; list = null;
		Mysql mysql = Mysql.getConnection();	//호출	
		sql = "select * from 요식업소 where 상권업종중분류코드='q09'";
		mysql.sql(sql);
		rs = mysql.select();
		
		list = new ArrayList<Store>();
		while(rs.next()) {
			Integer storeNumber = rs.getInt("상가업소번호");
			String storeName = rs.getString("상호명");
			String storeLocation = rs.getString("지점명");
			String codeMedium = rs.getString("상권업종중분류코드");
			String codeSmall = rs.getString("상권업종소분류코드");
			String storeAddress = rs.getString("도로명주소");
			double kDegree = rs.getDouble("경도");
			double wDegree = rs.getDouble("위도");
			Store s = new Store(storeNumber,storeName,storeLocation,codeMedium,codeSmall,storeAddress,kDegree,wDegree);
			list.add(s);
		}
		return list;
	}
	
	
	//위도경도로 조회하기
	public ArrayList<Store> whereAllmeal(double x, double y,String classsql) throws SQLException
	{
		
		double xa =x-0.1;
		double xb =x+0.1;
		double ya = y-0.1;
		double yb = y+0.1;
		rs = null; list = null;
		Mysql mysql = Mysql.getConnection();	//호출	
		sql ="select 도로명주소,상가업소번호,상호명,경도,위도 from 요식업소 where 경도 between "+xa+" and "+
				xb+" and "+" 위도 between "+
				ya+" and "+yb+classsql;
		mysql.sql(sql);
		rs = mysql.select();	

		list = new ArrayList<Store>();
		while(rs.next()) {
			String storeAddress = rs.getString("도로명주소");
			String storeName = rs.getString("상호명");
			Integer storeNumber =rs.getInt("상가업소번호");
			double kDegree = rs.getDouble("경도");
			double wDegree = rs.getDouble("위도");
			//xy는 내 위치
			double disx = kDegree;
			double disy = wDegree;
			double theta = y - disy;
			double dist = Math.sin(Math.toRadians(x)) * Math.sin(Math.toRadians(disx))
					+ Math.cos(Math.toRadians(x)) * Math.cos(Math.toRadians(disx))
					* Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			dist = dist*1.609344;
			Store s = new Store(storeName,kDegree,wDegree,dist,storeAddress,storeNumber);
	
			list.add(s);
			
			
		}
		return list;
	}
	
	public static void sort(ArrayList<Integer> list) {
	    sort(list, 0, list.size() - 1);
	}

	public static void sort(ArrayList<Integer> list, int from, int to) {
	    if (from < to) {
	        int pivot = from;
	        int left = from + 1;
	        int right = to;
	        int pivotValue = list.get(pivot);
	        while (left <= right) {
	            // left <= to -> limit protection
	            while (left <= to && pivotValue >= list.get(left)) {
	                left++;
	            }
	            // right > from -> limit protection
	            while (right > from && pivotValue < list.get(right)) {
	                right--;
	            }
	            if (left < right) {
	                Collections.swap(list, left, right);
	            }
	        }
	        Collections.swap(list, pivot, left - 1);
	        sort(list, from, right - 1); // <-- pivot was wrong!
	        sort(list, right + 1, to);   // <-- pivot was wrong!
	    }
	}
	
	
	
	
	//리뷰조회
	public ArrayList<Store> review(Integer stnum) throws SQLException
	{
		
		System.out.println("디비에서 가져올 stnum=="+stnum);
		rs = null; list = null;
		Mysql mysql = Mysql.getConnection();	//호출	
		sql ="select 회원아이디, 상가업소번호,리뷰내용,별점,리뷰번호,리뷰삭제일시 from 리뷰 where 상가업소번호 = "+stnum +" and 리뷰삭제일시 is NULL";
		mysql.sql(sql);
		rs = mysql.select();	

		list = new ArrayList<Store>();
		while(rs.next()) {

			String ID=rs.getString("회원아이디");
			System.out.println("아이디"+ID);
			Integer storeNumber =rs.getInt("상가업소번호");
			String review = rs.getString("리뷰내용");
			Integer eval =rs.getInt("별점"); 
			Integer reviewNum = rs.getInt("리뷰번호");
			

			Store s = new Store(ID,storeNumber,review,eval,reviewNum);
	
			list.add(s);
			
		}
		return list;
	}
	
	
	//리뷰작성
	public String updatereview(Integer stnum,String reviewtext, String eval) throws SQLException
	{
		
		
		
		Mysql mysql = Mysql.getConnection();	//호출	//아이디 수정해야댐 여기
		sql ="INSERT INTO 리뷰(리뷰번호,회원아이디,상가업소번호,리뷰내용,별점,리뷰수정일시,리뷰삭제일시,리뷰작성일시) VALUES ( null"+",'"+MainController.USER+"'"+",'"+stnum+"'"
				+",'"+reviewtext+"'"+",'"+Integer.parseInt(eval)+"', null,null,now())";
		//sql ="INSERT INTO 리뷰(리뷰번호,회원아이디,상가업소번호,리뷰내용,별점,리뷰작성일시,리뷰수정일시,리뷰삭제일시) VALUES ( null,'a1','23238319','왜안대','4','2019-05-13',NULL,NULL)";
		mysql.sql(sql);
		mysql.update();
		
		return "updateCom";

	}
	
	//리뷰수정
	public String Editreview(Integer reviewNum,String reviewtext, String eval) throws SQLException
	{
		
		
		
		Mysql mysql = Mysql.getConnection();	//호출	//아이디 수정해야댐 여기
		sql ="UPDATE 리뷰 SET 리뷰내용="+"'"+reviewtext+"'"+",리뷰수정일시 = "+ "now()"+",별점  ="+Integer.parseInt(eval)+ " WHERE 리뷰번호 ="+reviewNum;
		mysql.sql(sql);
		mysql.update();
		//sql ="UPDATE 리뷰 SET 별점="+Integer.parseInt(eval)+"WHERE 리뷰번호 ="+reviewNum;
		//mysql.sql(sql);
		//mysql.update();
		
		return "updateCom";

	}
	
	//리뷰삭제
	public String delreview(Integer reviewNum) throws SQLException
	{
		Mysql mysql = Mysql.getConnection();	//호출	//아이디 수정해야댐 여기
		sql ="UPDATE 리뷰 SET 리뷰삭제일시="+"now()"+ "WHERE 리뷰번호 = "+reviewNum;
		mysql.sql(sql);
		mysql.update();


		return "updateCom";
		
	}
	
	//짐하기
	public String inter(Integer reviewNum) throws SQLException
	{
		Mysql mysql = Mysql.getConnection();	//호출	//아이디 수정해야댐 여기
		sql ="DELETE FROM 리뷰 WHERE 리뷰번호 = "+reviewNum;
		mysql.sql(sql);
		mysql.update();


		return "updateCom";
		
	}
	
	//스토어 넘버로 스토어찾기
	public String addr(Integer storeNumber)throws SQLException
	{
		String addr = null;
		rs =null;
		Mysql mysql = Mysql.getConnection();
		sql ="select 도로명주소 from 요식업소 where 상가업소번호 ="+storeNumber;
		mysql.sql(sql);
		rs = mysql.select();	
		mysql.sql(sql);
		if (rs.isBeforeFirst()==true) {
			rs.next();
			 addr= rs.getString("도로명주소");

					
		}
		return addr;	
		
	}
	
	 
}


