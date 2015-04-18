package com.vituel.dndplayer.model.character;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.item.EquipSlot;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static com.vituel.dndplayer.model.item.SlotType.ARMS;
import static com.vituel.dndplayer.model.item.SlotType.BODY;
import static com.vituel.dndplayer.model.item.SlotType.EYES;
import static com.vituel.dndplayer.model.item.SlotType.FEET;
import static com.vituel.dndplayer.model.item.SlotType.FINGER;
import static com.vituel.dndplayer.model.item.SlotType.HANDS;
import static com.vituel.dndplayer.model.item.SlotType.HEAD;
import static com.vituel.dndplayer.model.item.SlotType.HELD;
import static com.vituel.dndplayer.model.item.SlotType.NECK;
import static com.vituel.dndplayer.model.item.SlotType.SHOULDERS;
import static com.vituel.dndplayer.model.item.SlotType.TORSO;
import static com.vituel.dndplayer.model.item.SlotType.WAIST;

/**
 * See DM Guide, p.214
 * <p/>
 * Created by Victor on 29/04/14.
 */
public class CharEquip implements Serializable {

    private EquipSlot mainHand;
    private EquipSlot offhand;
    private EquipSlot body;
    private EquipSlot head;
    private EquipSlot eyes;
    private EquipSlot neck;
    private EquipSlot torso;
    private EquipSlot waist;
    private EquipSlot shoulders;
    private EquipSlot arms;
    private EquipSlot hands;
    private EquipSlot finger1;
    private EquipSlot finger2;
    private EquipSlot feet;

    public CharEquip() {
        setMainHand(new EquipSlot(R.string.main_hand, HELD));
        setOffhand(new EquipSlot(R.string.offhand, HELD));
        setBody(new EquipSlot(R.string.body, BODY));
        setHead(new EquipSlot(R.string.head, HEAD));
        setEyes(new EquipSlot(R.string.eyes, EYES));
        setNeck(new EquipSlot(R.string.neck, NECK));
        setTorso(new EquipSlot(R.string.torso, TORSO));
        setWaist(new EquipSlot(R.string.waist, WAIST));
        setShoulders(new EquipSlot(R.string.shoulders, SHOULDERS));
        setArms(new EquipSlot(R.string.arms, ARMS));
        setHands(new EquipSlot(R.string.hands, HANDS));
        setFinger1(new EquipSlot(R.string.finger1, FINGER));
        setFinger2(new EquipSlot(R.string.finger2, FINGER));
        setFeet(new EquipSlot(R.string.feet, FEET));
    }

    public List<EquipSlot> listEquip() {
        List<EquipSlot> list = new ArrayList<>();
        list.add(getMainHand());
        list.add(getOffhand());
        list.add(getBody());
        list.add(getHead());
        list.add(getEyes());
        list.add(getNeck());
        list.add(getTorso());
        list.add(getWaist());
        list.add(getShoulders());
        list.add(getArms());
        list.add(getHands());
        list.add(getFinger1());
        list.add(getFinger2());
        list.add(getFeet());
        return list;
    }

    public EquipSlot getByName(int nameRes) {
        switch (nameRes) {
            case R.string.main_hand:
                return getMainHand();
            case R.string.offhand:
                return getOffhand();
            case R.string.body:
                return getBody();
            case R.string.head:
                return getHead();
            case R.string.eyes:
                return getEyes();
            case R.string.neck:
                return getNeck();
            case R.string.torso:
                return getTorso();
            case R.string.waist:
                return getWaist();
            case R.string.shoulders:
                return getShoulders();
            case R.string.arms:
                return getArms();
            case R.string.hands:
                return getHands();
            case R.string.finger1:
                return getFinger1();
            case R.string.finger2:
                return getFinger2();
            case R.string.feet:
                return getFeet();
            default:
                throw new InvalidParameterException("" + nameRes);
        }
    }

    public CharEquip clone() {
        CharEquip copy = new CharEquip();
        copy.setMainHand(getMainHand().clone());
        copy.setOffhand(getOffhand().clone());
        copy.setBody(getBody().clone());
        copy.setHead(getHead().clone());
        copy.setEyes(getEyes().clone());
        copy.setNeck(getNeck().clone());
        copy.setTorso(getTorso().clone());
        copy.setWaist(getWaist().clone());
        copy.setShoulders(getShoulders().clone());
        copy.setArms(getArms().clone());
        copy.setHands(getHands().clone());
        copy.setFinger1(getFinger1().clone());
        copy.setFinger2(getFinger2().clone());
        copy.setFeet(getFeet().clone());
        return copy;
    }

    public EquipSlot getMainHand() {
        return mainHand;
    }

    public void setMainHand(EquipSlot mainHand) {
        this.mainHand = mainHand;
    }

    public EquipSlot getOffhand() {
        return offhand;
    }

    public void setOffhand(EquipSlot offhand) {
        this.offhand = offhand;
    }

    public EquipSlot getBody() {
        return body;
    }

    public void setBody(EquipSlot body) {
        this.body = body;
    }

    public EquipSlot getHead() {
        return head;
    }

    public void setHead(EquipSlot head) {
        this.head = head;
    }

    public EquipSlot getEyes() {
        return eyes;
    }

    public void setEyes(EquipSlot eyes) {
        this.eyes = eyes;
    }

    public EquipSlot getNeck() {
        return neck;
    }

    public void setNeck(EquipSlot neck) {
        this.neck = neck;
    }

    public EquipSlot getTorso() {
        return torso;
    }

    public void setTorso(EquipSlot torso) {
        this.torso = torso;
    }

    public EquipSlot getWaist() {
        return waist;
    }

    public void setWaist(EquipSlot waist) {
        this.waist = waist;
    }

    public EquipSlot getShoulders() {
        return shoulders;
    }

    public void setShoulders(EquipSlot shoulders) {
        this.shoulders = shoulders;
    }

    public EquipSlot getArms() {
        return arms;
    }

    public void setArms(EquipSlot arms) {
        this.arms = arms;
    }

    public EquipSlot getHands() {
        return hands;
    }

    public void setHands(EquipSlot hands) {
        this.hands = hands;
    }

    public EquipSlot getFinger1() {
        return finger1;
    }

    public void setFinger1(EquipSlot finger1) {
        this.finger1 = finger1;
    }

    public EquipSlot getFinger2() {
        return finger2;
    }

    public void setFinger2(EquipSlot finger2) {
        this.finger2 = finger2;
    }

    public EquipSlot getFeet() {
        return feet;
    }

    public void setFeet(EquipSlot feet) {
        this.feet = feet;
    }
}
