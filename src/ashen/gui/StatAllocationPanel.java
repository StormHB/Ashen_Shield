package ashen.gui;

import ashen.creation.StatAllocation;
import ashen.model.Ability;
import ashen.model.Stats;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.Map;

/**
 * Panel responsible for point-buy stat allocation during character creation.
 */
public class StatAllocationPanel extends JPanel {

    private static final Color DEFAULT_STAT_COLOR = Color.BLACK;
    private static final Color LIGHT_BONUS_COLOR = new Color(180, 140, 0);
    private static final Color STRONG_BONUS_COLOR = new Color(0, 180, 0);

    private final StatAllocation statAllocation = new StatAllocation();
    private final Map<Ability, JLabel> statValueLabels = new EnumMap<>(Ability.class);
    private final Map<Ability, JLabel> statModifierLabels = new EnumMap<>(Ability.class);

    private Stats raceBonuses = new Stats(0, 0, 0, 0, 0, 0);
    private JLabel pointsRemainingLabel;

    public StatAllocationPanel() {
        layoutComponents();
    }

    public int getPointsRemaining() {
        return statAllocation.getPointsRemaining();
    }

    public void applyRaceBonuses(Stats raceBonuses) {
        this.raceBonuses = raceBonuses;
        statAllocation.applyRaceBonuses(raceBonuses);
        updateStatLabels();
    }

    public Stats toStats() {
        return statAllocation.toStats();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        JPanel statsPanel = new JPanel(new GridBagLayout());

        pointsRemainingLabel = new JLabel("Points Remaining: " + statAllocation.getPointsRemaining());
        pointsRemainingLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        statsPanel.add(pointsRemainingLabel, gbc);

        gbc.gridwidth = 1;

        int row = 1;

        for (Ability ability : Ability.values()) {
            addStatRow(statsPanel, gbc, row, ability);
            row++;
        }

        JButton randomizeButton = new JButton("Randomize Stats");
        randomizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randomizeStats();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 5;
        gbc.insets = new Insets(20, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        statsPanel.add(randomizeButton, gbc);

        add(statsPanel, BorderLayout.NORTH);
    }

    private void addStatRow(JPanel panel, GridBagConstraints gbc, int row, Ability ability) {
        JButton minusButton = new JButton("-");
        JButton plusButton = new JButton("+");
        JLabel valueLabel = new JLabel(String.valueOf(statAllocation.getValue(ability)));
        JLabel modifierLabel = new JLabel(
                Stats.formatModifier(Stats.calculateModifier(statAllocation.getValue(ability)))
        );

        statValueLabels.put(ability, valueLabel);
        statModifierLabels.put(ability, modifierLabel);

        minusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decreaseStat(ability);
            }
        });
        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                increaseStat(ability);
            }
        });

        gbc.gridy = row;

        gbc.gridx = 0;
        panel.add(new JLabel(ability.getAbbreviation()), gbc);

        gbc.gridx = 1;
        panel.add(minusButton, gbc);

        gbc.gridx = 2;
        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        valueLabel.setPreferredSize(new Dimension(30, 25));
        panel.add(valueLabel, gbc);

        gbc.gridx = 3;
        panel.add(plusButton, gbc);

        gbc.gridx = 4;
        modifierLabel.setPreferredSize(new Dimension(30, 25));
        panel.add(modifierLabel, gbc);
    }

    private void increaseStat(Ability ability) {
        if (statAllocation.increase(ability)) {
            updateStatLabels();
        }
    }

    private void decreaseStat(Ability ability) {
        if (statAllocation.decrease(ability)) {
            updateStatLabels();
        }
    }

    private void updatePointsRemainingLabel() {
        pointsRemainingLabel.setText("Points Remaining: " + statAllocation.getPointsRemaining());
    }

    private void updateModifierLabels() {
        for (Ability ability : Ability.values()) {
            JLabel modifierLabel = statModifierLabels.get(ability);
            int statValue = statAllocation.getValue(ability);

            modifierLabel.setText(Stats.formatModifier(Stats.calculateModifier(statValue)));
        }
    }

    private void randomizeStats() {
        statAllocation.randomize();
        statAllocation.applyRaceBonuses(raceBonuses);
        updateStatLabels();
    }

    private void updateStatLabels() {
        for (Ability ability : Ability.values()) {
            statValueLabels.get(ability).setText(String.valueOf(statAllocation.getValue(ability)));
        }

        updatePointsRemainingLabel();
        updateModifierLabels();
        updateStatColors();
    }

    private void updateStatColors() {
        for (Ability ability : Ability.values()) {
            setStatColor(statValueLabels.get(ability), statAllocation.getRaceBonus(ability));
        }
    }

    private void setStatColor(JLabel label, int bonus) {
        if (bonus >= 2) {
            label.setForeground(STRONG_BONUS_COLOR);
        } else if (bonus == 1) {
            label.setForeground(LIGHT_BONUS_COLOR);
        } else {
            label.setForeground(DEFAULT_STAT_COLOR);
        }
    }
}
