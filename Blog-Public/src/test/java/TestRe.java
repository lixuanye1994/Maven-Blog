import com.blogdemo.domain.entity.Article;
import com.blogdemo.domain.vo.HotArticleListVO;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static com.blogdemo.utils.BeanCopyUtilsVO.copyBean;

/**
 * 反射测试
 * getFields() 返回public属性的成员属性
 * getDeclaredFields() 返回全部成员属性变量
 */
public class TestRe {
    public static void main(String[] args){

        //    测试先行
//        Article article = new Article();
//        article.setId(1L);
//        article.setTitle("dwadwad");
//        article.setViewCount(999L);
//
//        HotArticleListVO hotArticleList = new HotArticleListVO();
//        BeanUtils.copyProperties(article,hotArticleList);
//
//        System.out.println(hotArticleList.getId());
//        System.out.println(hotArticleList.getViewCount());
//        System.out.println(hotArticleList.getTitle());
        String a = (String) TestObject1("hahahha");
        int b = (int)TestObject1(1111);

        System.out.println("-----------------");

        String aa = TestObject2("hohoho");
        int bb = TestObject2(222);

    }

    public static Object TestObject1(Object scores){
        System.out.println(scores);
        return scores;
    }
    public static <V> V TestObject2(V scores){
        System.out.println(scores);
        return scores;
    }

}
