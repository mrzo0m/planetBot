package com.github.br.starmarines.gameui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.collections.ObservableList;

import com.github.br.starmarines.ui.api.IUiComponent;
import com.github.br.starmarines.ui.api.IUiOrderComponent;

/**
 * https://groups.google.com/forum/#!topic/wisdom-discuss/hoaxFBs84Dw - суть
 * проблемы
 * http://felix.apache.org/ipojo/api/1.12.1/org/apache/felix/ipojo/manipulator
 * /metadata/annotation/visitor/bind/MethodBindVisitor.html
 * 
 * https://groups.google.com/forum/#!topic/wisdom-discuss/hoaxFBs84Dw
 * https://groups.google.com/forum/#!msg/wisdom-discuss/5-rHPjhNdOA/9MwMyokGN_UJ
 * хотели добавить возможность искать аннотации в суперклассах в 15 году, но
 * версия ipojo ни в 15, ни в 16 году так и не вышла
 * 
 * @author burning rain
 *
 * @param <FXPARENT>
 *            =куда вкладываем= javafx-элемент, к которому цепляются элементы из
 *            IUI.getNode()
 * @param <FXCHILD>
 *            =что вкладываем=  тип javafx-элемента, который указан в
 *            наследнике {@link IUiComponent}
 * @param <ISERVICE>
 *            =откуда берем то, что вкладываем= {@link IUiComponent} и его
 *            производные
 */
public abstract class AbstractOrderController<FXPARENT, FXCHILD, ISERVICE extends IUiOrderComponent<FXCHILD>>
		implements IUiComponent<FXPARENT> {

	private FXPARENT fxParent;
	private ObservableList<FXCHILD> fxChildren;
	private ConcurrentMap<String, PairFxService<FXPARENT, FXCHILD, ISERVICE>> fxPairMap = new ConcurrentHashMap<>();

	public void init(FXPARENT component, ObservableList<FXCHILD> children) {
		this.fxParent = component;
		this.fxChildren = children;
	}

	public PairFxService<FXPARENT, FXCHILD, ISERVICE> bind(ISERVICE uiComponentImpl) {
		if (getNode() != null) {
			String key = generateUID(uiComponentImpl);
			if (fxPairMap.containsKey(key)) {
				throw new RuntimeException(String.format(
						"Элемент %s '%s' уже существует", uiComponentImpl
								.getClass().getCanonicalName(), uiComponentImpl
								.toString()));
			}
			
			PairFxService<FXPARENT, FXCHILD, ISERVICE> pairFxService = new PairFxService<>(
					uiComponentImpl, fxParent);			
			Platform.runLater(() -> {
				addToUI(new ArrayList<PairFxService<FXPARENT, FXCHILD, ISERVICE>>(
						fxPairMap.values()), fxChildren);
			});
			fxPairMap.put(key, pairFxService);
			return pairFxService;
		} else {
			throw new RuntimeException(String.format(
					"Компонент интерфейса '%s' не был зарегистрирован!",
					uiComponentImpl.getClass().getCanonicalName()));
		}
		
	}

	protected void addToUI(
			List<PairFxService<FXPARENT, FXCHILD, ISERVICE>> allUiPairs,
			ObservableList<FXCHILD> children) {
		Collections.sort(allUiPairs);
		children.clear();
		List<FXCHILD> elements = allUiPairs.stream().map(a -> a.getFxChild())
				.collect(Collectors.toList());
		children.addAll(elements);
		System.out.println(children);
	}

	public PairFxService<FXPARENT, FXCHILD, ISERVICE> unbind(ISERVICE uiComponentImpl) {
		String key = generateUID(uiComponentImpl);
		PairFxService<FXPARENT, FXCHILD, ISERVICE> fxPair = fxPairMap.get(key);
		if (fxPair != null) {			
			Platform.runLater(() -> {
				deleteFromUI(fxPair, fxChildren);
			});
			fxPairMap.remove(key);
			return fxPair;
		} else {
			throw new RuntimeException(String.format(
					"Компонент интерфейса '%s' не был зарегистрирован!",
					uiComponentImpl.getClass().getName()));
		}
	}

	protected void deleteFromUI(
			PairFxService<FXPARENT, FXCHILD, ISERVICE> fxPair,
			ObservableList<FXCHILD> children) {
		Optional<FXCHILD> menuElement = children.stream()
				.filter(m -> m.equals(fxPair.getFxChild())).findAny();
		if (menuElement.isPresent()) {
			children.remove(menuElement.get());
		} else {
			throw new RuntimeException(String.format(
					"Элемент интерфейса '%s' (%s) не найден!",
					generateUID(fxPair.getService()), fxPair.getService().toString()));
		}
	}

	@Override
	public FXPARENT getNode() {
		return this.fxParent;
	}

	protected abstract String generateUID(ISERVICE uiComponentImpl);

}