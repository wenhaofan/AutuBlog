package com.autu.common.aop;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jfinal.core.ControllerFactory;

/**
 * AopControllerFactory 用于注入依赖对象并更好支持 AOP，其优点如下：
 * 1：使用 @Inject 自动化注入并 enhance 对象，免去业务层 AOP 必须手动 enhance 的步骤
 * 
 * 2：免去业务层维护单例的样板式代码，例如下面代码可以删掉了：
 *    public static final MyService me = new MyService();
 *    
 * 
 * TODO: 考虑 jfinal 3.5 默认使用 AopControllerFactory
 * 
 * 
 * 基本用法如下：
 * 1：配置
 *    me.setControllerFactory(new AopControllerFactory());
 *    
 * 2：Controller 中注入业务层，也可以注入任何其它类，不一定非得是 Service
 *    public class MyController extends Controller {
 *    
 *       @Inject
 *       MyService service;
 *       
 *       public void index() {
 *          render(service.doIt());
 *       }
 *    }
 *    
 * 3：Service 注入另一个 Service，也可以注入任何其它类，不一定非得是 Service
 *    public class MyService {
 *    
 *       @Inject
 *       OtherService other;		// OtherService 内部还可以继续接着注入
 *       
 *       public void doIt() {
 *          other.doOther();
 *       }
 *    }
 * 
 * 如上代码所示，在使用时，只需要在被注入的对象上使用 @Inject 即可，代码量极少，学习成本极低
 * 
 * 
 * 高级用法：
 * 1：@Inject 注解默认注入属性自身类型的对象，可以通过如下代码指定被注入的类型：
 *    @Inject(UserServiceImpl.class)			// 此处的 UserServiceImpl 为 UserService 的子类或实现类
 *    UserService userService;
 * 
 * 2：被注入对象默认会被 enhance 增强，可以通过 AopControllerFactory.setEnhance(false) 配置默认不增强
 * 
 * 3：被注入对象默认是 singleton 单例，可以通过 AopControllerFactory.setSingleton(false) 配置默认不为单例
 * 
 * 4：可以在 @Inject 注解中直接配置 enhance 增强与 singleton 单例：
 *    @Inject(enhance=YesOrNo.NO, singleton=YesOrNo.YES)
 *    注意：如上在 @Inject 直接配置会覆盖掉 2、3 中 setEnhance()/setSingleton() 方法配置的默认值
 * 
 * 5：如上 2、3、4 中的配置，建议的用法是：先用 setEnhance()/setSingleton() 配置大多数情况，然后在个别
 *    违反上述配置的情况下在 @Inject 中直接 enhance、singleton 来覆盖默认配置，这样可以节省大量代码
 */
public class AopFactory extends ControllerFactory {
	
	// 单例缓存
	protected HashMap<Class<?>, Object> singletonCache = new HashMap<Class<?>, Object>();
	
	private YesOrNo enhance = YesOrNo.NO;			// 默认增强
	private YesOrNo singleton = YesOrNo.YES;			// 默认单例
	private static int injectDepth = 5;						// 默认注入深度
	
	private static AopFactory me;
	
	@SuppressWarnings("unchecked")
	public static <T> T getInject(Class<? extends Object> target){
		Object obj=null;
		try {
			obj = target.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(me==null) {
			me=new 	AopFactory();
		}
		
		try {
			me.inject(injectDepth, target, obj);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (T) obj;
	}
	
	/**
	 * 设置被注入的对象是否被增强，可使用 @Inject(enhance = YesOrNo.NO) 覆盖此默认值
	 */
	public AopFactory setEnhance(boolean enhance) {
		this.enhance = enhance ? YesOrNo.YES : YesOrNo.NO;
		return this;
	}
	
	/**
	 * 设置被注入的对象是否为单例，可使用 @Inject(singleton = YesOrNo.NO) 覆盖此默认值 
	 */
	public AopFactory setSingleton(boolean singleton) {
		this.singleton = singleton ? YesOrNo.YES : YesOrNo.NO;
		return this;
	}
	
	/**
	 * 设置注入深度，避免被注入类在具有循环依赖时造成无限循环
	 */
	public AopFactory setInjectDepth(int injectDepth) {
		if (injectDepth <= 0) {
			throw new IllegalArgumentException("注入层数必须大于 0");
		}
		if (injectDepth >= 7) {
			throw new IllegalArgumentException("为保障性能，注入层数必须小于 7");
		}
		
		AopFactory.injectDepth = injectDepth;
		return this;
	}
	
	@Override
	public Controller getController(Class<? extends Controller> controllerClass) throws InstantiationException, IllegalAccessException {
		Controller c = controllerClass.newInstance();
		inject(injectDepth, controllerClass, c);
		return c;
	}
	
	public void inject(int injectDepth, Class<?> targetClass, Object targetObject) throws IllegalAccessException, InstantiationException {
		if ((injectDepth--) <= 0) {
			return ;
		}
		
		targetClass = getUsefulClass(targetClass);
		
		List<Field> filedList=new ArrayList<>();

		Field[] fields = targetClass.getFields();
		
		for(Field field:fields) {
			filedList.add(field);
		}
		fields=targetClass.getDeclaredFields();
		
		for(Field field:fields) {
			filedList.add(field);
		}
		if (filedList.isEmpty()) {
			return ;
		}
		
		for (Field field : filedList) {
			Inject inject = field.getAnnotation(Inject.class);
			if (inject == null) {
				continue ;
			}
			
			Class<?> fieldInjectedClass = inject.value();
			if (fieldInjectedClass == Void.class) {
				fieldInjectedClass = field.getType();
			}
			
			YesOrNo enhance = inject.enhance();
			if (enhance == YesOrNo.DEFAULT) {
				enhance = this.enhance;
			}
			
			YesOrNo singleton = inject.singleton();
			if (singleton == YesOrNo.DEFAULT) {
				singleton = this.singleton;
			}
			
			Object fieldInjectedObject = getInjectedObject(fieldInjectedClass, enhance, singleton);
			field.setAccessible(true);
			field.set(targetObject, fieldInjectedObject);
			
			// 递归调用，为当前被注入的对象进行注入
			this.inject(injectDepth, fieldInjectedObject.getClass(), fieldInjectedObject);
		}
	}
	
	private Object getInjectedObject(Class<?> injectedClass, YesOrNo enhance, YesOrNo singleton) throws InstantiationException, IllegalAccessException {
		if (singleton == YesOrNo.NO) {
			return createInjectedObject(injectedClass, enhance);
		}
		
		Object ret = singletonCache.get(injectedClass);
		if (ret == null) {
			synchronized(injectedClass) {
				ret = singletonCache.get(injectedClass);
				if (ret == null) {
					ret = createInjectedObject(injectedClass, enhance);
					singletonCache.put(injectedClass, ret);
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * 由于上层已经处理过 singleton，所以 Enhancer.enhance() 方法中不必关心 singleton
	 */
	private Object createInjectedObject(Class<?> injectedClass, YesOrNo enhance) throws InstantiationException, IllegalAccessException {
		return (enhance == YesOrNo.YES) ? Enhancer.enhance(injectedClass) : injectedClass.newInstance();
	}
	
	/**
	 * 被 cglib、guice 增强过的类需要通过本方法获取到被增强之前的类型
	 * 否则调用其 targetClass.getDeclaredFields() 方法时
	 * 获取到的是一堆 cglib guice 生成类中的 Field 对象
	 * 而被增强前的原类型中的 Field 反而获取不到
	 */
	protected Class<?> getUsefulClass(Class<?> clazz) {
		// com.demo.blog.Blog$$EnhancerByCGLIB$$69a17158
		// return (Class<? extends Model>)((modelClass.getName().indexOf("EnhancerByCGLIB") == -1 ? modelClass : modelClass.getSuperclass()));
		return (Class<?>)(clazz.getName().indexOf("$$EnhancerBy") == -1 ? clazz : clazz.getSuperclass());
	}
}




