package com.sinosoft.one.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sinosoft.one.test.rule.domain.*;
import com.sinosoft.one.test.rule.service.spring.UndwrtService;
import com.sinosoft.undwrt.undwrtRule.service.UndwrtRuleRiskKind;
import com.sinosoft.undwrt.undwrtRule.vo.BusinessProposalData;
import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import com.sinosoft.one.quickprice.domain.CarInfoInputBOM;
import com.sinosoft.one.quickprice.domain.KindBOM;
import com.sinosoft.one.quickprice.domain.QuickPriceInputGlobal;
import com.sinosoft.one.test.rule.model.PdCombo;
import com.sinosoft.one.test.rule.model.PdComboPack;
import com.sinosoft.one.test.rule.model.ProKind;
import com.sinosoft.one.test.rule.model.ProOrder;
import com.sinosoft.one.test.rule.model.ProVehicle;
import com.sinosoft.one.test.rule.service.facade.ComboRuleService;
import com.sinosoft.one.test.rule.service.facade.GuvnorRuleService;
import com.sinosoft.one.test.rule.service.facade.OtherRuleService;
import com.sinosoft.one.test.rule.service.facade.ProposalCheck;
import com.sinosoft.one.test.rule.service.facade.ProposalRuleService;
import com.sinosoft.one.test.rule.service.facade.QuickPriceRuleService;
import com.sinosoft.one.test.rule.service.facade.SpecialClausRuleService;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@DirtiesContext
@ContextConfiguration(locations = { "/applicationContext-test.xml",
		"/spring/applicationContext-rule-test.xml" })
public class RuleServiceTest extends AbstractJUnit4SpringContextTests {
	@Autowired
	public ComboRuleService comboRuleService;
	@Autowired
	public OtherRuleService otherRuleService;
	@Autowired
	public GuvnorRuleService guvnorRuleService;
	@Autowired
	public QuickPriceRuleService quickPriceRuleService;
	@Autowired
	public SpecialClausRuleService specialClausRuleService;
	@Autowired
	public ProposalRuleService proposalRuleService;
    @Autowired
    private UndwrtService undwrtService;

	@Test
	public void testQuickPriceService() {
		CarInfoInputBOM qpib = new CarInfoInputBOM();
		qpib.setAreaCode("370000");
		qpib.setVehicleAge(1);
		qpib.setReplacementValue(20000.00);
		QuickPriceInputGlobal global = new QuickPriceInputGlobal();
		double totalPremium = 0.00;
		try {
			System.out.println("=========begin QuickPriceRule");
			quickPriceRuleService.executeRuleWithGlobal("quickPriceRuleFlow",
					global, qpib);
			// quickPriceRuleService.executeRule( qpib);
			System.out.println("=========end QuickPriceRule");

			Iterator iter = global.getKinds().entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String kindcode = (String) entry.getKey();
				KindBOM kind = (KindBOM) entry.getValue();
				System.out.println(kindcode + " name=" + kind.getKindName());
				System.out.print(kindcode + "amount=" + kind.getAmount());
				System.out.print(kindcode + "premium=" + kind.getPremium());
				double premium = new BigDecimal(kind.getPremium()
						* global.getDiscount()).setScale(2,
						BigDecimal.ROUND_HALF_UP).doubleValue();
				System.out.println(kindcode + "discount premium=" + premium);
				totalPremium += premium;
			}
			System.out.println(" totalPremium  " + totalPremium);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertTrue(totalPremium > 0.0);
	}

	@Test
	public void testproposalService() {

		ProPosalInputBOM ppib = new ProPosalInputBOM();
		ppib.setAreaCode("010");
		ppib.setCityCode("000");
		ppib.setProPosal(true);
		ProVehicle pv = new ProVehicle();
		pv.setIsNewVehicle("01");
		pv.setModelCode("VC1234567890123456");
		pv.setModelName("本田雅阁");
		pv.setUseNatureCode("01");
		pv.setReplacementValue(new BigDecimal(800000.00));
		ppib.setProVehicle(pv);
		proposalRuleService.executeRule(ppib);
		System.out.println("车牌为VC1234567890123456 是否可以投保" + ppib.isProPosal());
		Assert.assertFalse(ppib.isProPosal());
		try {
			System.out.println("=========in sleep ");
			System.out.println("=========请修改规则表");
			Thread.sleep(30 * 1000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ppib.setAreaCode("010");
		ppib.setCityCode("005");
		ppib.setProPosal(true);
		pv.setIsNewVehicle("01");
		pv.setModelCode("VC12343456");
		pv.setModelName("本田雅阁");
		pv.setUseNatureCode("01");
		pv.setReplacementValue(new BigDecimal(800000.00));
		ppib.setProVehicle(pv);
		proposalRuleService.executeRule(ppib);
		System.out.println("车牌为VC12343456 是否可以投保" + ppib.isProPosal());
		Assert.assertTrue(ppib.isProPosal());
	}

	@Test
	public void testComboService() {
		boolean testOk = false;

		ProposalCheck pc = new ProposalCheck();
		pc.setCarAge(90);
		pc.setPurchasePrice("60000");
		String areaCode = "370000";
		try {
			PdCombo pd = new PdCombo();
			String code = "";
			List<PdComboPack> pdComboPacks = new ArrayList<PdComboPack>();
			for (int i = 1; i < 5; i++) {
				PdComboPack pdComboPack = new PdComboPack();
				pdComboPack.setAllowProposal(true);
				pdComboPack.setDefaultValue("1000");
				if (i == 1)
					code = "030006";
				if (i == 2)
					code = "030059";
				if (i == 3)
					code = "030012";
				if (i == 4)
					code = "030025";
				pd.addKindCodePack(code, pdComboPack);
				// pd.getPackByKindCode(new String("00" + i)).setAllowProposal(
				// false);
				pdComboPacks.add(pdComboPack);
			}
			pd.setPdComboPacks(pdComboPacks);
			comboRuleService.executeRuleWithGlobal("comboRuleFlow", pd,
					areaCode, pc);

			for (int i = 1; i < 5; i++) {
				if (i == 1)
					code = "030006";
				if (i == 2)
					code = "030059";
				if (i == 3)
					code = "030012";
				if (i == 4)
					code = "030025";
				String valueRange = pd.getPackByKindCode(code).getValueRange();
				if (null != valueRange) {
					testOk = true;
				}
				System.out.println("kind" + code + ":" + valueRange + "是否允许投保:"
						+ pd.getPackByKindCode(code).isAllowProposal());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertTrue(testOk);
	}

	@Test
	public void testSpecialClausService() {

		try {
			ProVehicle pv = new ProVehicle();
			pv.setIsNewVehicle("01");
			pv.setGlassType("303011001");
			pv.setUseNatureCode("359000");
			pv.setLicensePlateNo("121212");
			String areaCode = "37000000";
			KindInputBOM kib = new KindInputBOM();
			ProOrder po = new ProOrder();
			List<ProKind> proKinds = new ArrayList<ProKind>(0);
			ProKind pk1 = new ProKind();
			pk1.setKindCode("030004");
			proKinds.add(pk1);
			ProKind pk2 = new ProKind();
			pk2.setKindCode("030012");
			proKinds.add(pk2);
			ProKind pk3 = new ProKind();
			pk3.setKindCode("030059");
			// ProKind pk4 = new ProKind();
			// pk4.setKindCode("030050");
			proKinds.add(pk3);
			po.setProKinds(proKinds);

			kib.setPo(po);
			HashMap<String, String> global = new HashMap<String, String>();
			ArrayList<String> kinds = new ArrayList<String>();
			kinds.add("030004");
			kinds.add("030012");
			kinds.add("030059");
			specialClausRuleService.executeRule(global, pv, kib, areaCode);
			// global.put("001", "test");
			for (String key : global.keySet()) {
				System.out.println(key + ":" + global.get(key));
			}
			System.out.println("change your rule.....");
			Thread.sleep(30 * 1000);

			specialClausRuleService.executeRule(global, pv, kib, areaCode);
			// global.put("001", "test");
			for (String key : global.keySet()) {
				System.out.println(key + ":" + global.get(key));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Test
    public void testUndwrt(){
        UndwrtCondition condition = new UndwrtCondition();
        condition.setCarType("01");
        UndwrtRiskKind aKind = new UndwrtRiskKind();
        aKind.setKindCode("A");
        aKind.setAmount(1000000);
        condition.addRiskKind("A",aKind);
        UndwrtRiskKind bKind = new UndwrtRiskKind();
        bKind.setKindCode("B");
        bKind.setAmount(1000000);
        condition.addRiskKind("B",bKind);

        boolean result = undwrtService.undwrt("8",condition);

        System.out.println(result);
//        Assert.assertEquals(false,result);
//
//        UndwrtCondition condition2 = new UndwrtCondition();
//        condition2.setCarAge(10);
//        condition2.setCarNature("00");
//        condition2.setCarType("02");
//        boolean result2 = undwrtService.undwrt("8",condition2);
//        Assert.assertEquals(false,result);
    }

    @Test
    public void JFRuleFlowTest() {
        PrpUISechema prpDomain = new PrpUISechema();
        prpDomain.setComcode("2510000000");
        prpDomain.setRiskcode("0101");
        prpDomain.setExcepType("02");
        prpDomain.setClasscode("01");
        prpDomain.setSumpreminu(new BigDecimal(200000));
        prpDomain.setFirstpremium(new BigDecimal(60000));
        prpDomain.setPayTimes(3);
        prpDomain.setLastPayendate("2012-04-07");
        prpDomain.setEnddate("2012-06-28");

        try {
            undwrtService.executeRule(prpDomain);
            System.out.println(prpDomain.getjFeeResult());
        } catch (Exception e) {
            throw new RuntimeException("程序出错啦", e);
        }
    }

}
